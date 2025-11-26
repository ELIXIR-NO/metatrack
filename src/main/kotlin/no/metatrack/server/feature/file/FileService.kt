package no.metatrack.server.feature.file

import io.minio.MinioClient
import io.minio.PutObjectArgs
import no.metatrack.server.helper.MinioHelperService
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class FileService(
    private val minioClient: MinioClient,
    private val minioHelperService: MinioHelperService,
) {
    fun uploadFile(
        bucketName: String,
        objectName: String,
        fileStream: InputStream,
        contentType: String,
    ) {
        minioHelperService.ensureBucketExists(bucketName)

        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(fileStream, -1, 10485760)
                .contentType(contentType)
                .build(),
        )
    }
}
