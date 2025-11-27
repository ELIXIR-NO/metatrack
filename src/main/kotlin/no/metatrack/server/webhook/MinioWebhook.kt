package no.metatrack.server.webhook

import no.metatrack.server.feature.file.FileService
import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.ObjectMapper

@RestController
@RequestMapping("/minio-webhook")
class MinioWebhook(
    private val fileService: FileService,
    private val objectMapper: ObjectMapper,
    @Value($$"${minio.webhook.secret-key}") private val secretKey: String,
) {
    @PostMapping
    fun handleWebhook(
        @RequestHeader headers: Map<String, String>,
        @RequestBody event: MinioEvent,
    ): ResponseEntity<Unit> {
        if (!isValidSignature(headers, event)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        event.records?.forEach { record ->
            if (record.eventName.startsWith("s3:ObjectCreated")) {
                fileService.processUploadedFile(
                    bucket = record.s3.bucket.name,
                    objectName = record.s3.`object`.key,
                    objectSize = record.s3.`object`.size,
                )
            }
        }
        return ResponseEntity.ok().build()
    }

    private fun isValidSignature(
        headers: Map<String, String>,
        body: Any,
    ): Boolean {
        headers["x-minio-signature"]
        val payload = objectMapper.writeValueAsString(body)

        val computedSignature =
            HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey)
                .hmac(payload)
                .toString()

        return computedSignature.contentEquals(payload)
    }
}

data class MinioEvent(
    val eventName: String? = null,
    val key: String? = null,
    val records: List<MinioRecord>? = null,
)

data class MinioRecord(
    val eventName: String,
    val s3: MinioS3,
)

data class MinioS3(
    val bucket: MinioBucket,
    val `object`: MinioObject,
)

data class MinioBucket(
    val name: String,
)

data class MinioObject(
    val key: String,
    val size: Long,
)
