package no.metatrack.server.helper

import io.minio.BucketExistsArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.RemoveBucketArgs
import io.minio.http.Method
import no.metatrack.server.feature.project.ProjectService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MinioHelperService(
    private val minioClient: MinioClient,
    private val projectService: ProjectService,
) {
    fun generatePreSignedGetUrl(
        projectId: UUID,
        objectName: String,
        expiryMinutes: Int = 60,
    ): String {
        projectService.getProjectById(projectId) ?: throw NoSuchElementException("No such project")
        ensureBucketExists(projectId.toString())
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs
                .builder()
                .method(Method.GET)
                .bucket(projectId.toString())
                .`object`(objectName)
                .expiry(expiryMinutes * 60)
                .build(),
        )
    }

    fun generatePreSignedPutUrl(
        projectId: UUID,
        objectName: String,
        expiryMinutes: Int = 60,
    ): String {
        projectService.getProjectById(projectId) ?: throw NoSuchElementException("No such project")
        ensureBucketExists(projectId.toString())
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs
                .builder()
                .method(Method.PUT)
                .bucket(projectId.toString())
                .`object`(objectName)
                .expiry(expiryMinutes * 60)
                .build(),
        )
    }

    fun ensureBucketExists(bucketName: String): Boolean =
        try {
            val exists =
                minioClient.bucketExists(
                    BucketExistsArgs
                        .builder()
                        .bucket(bucketName)
                        .build(),
                )

            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs
                        .builder()
                        .bucket(bucketName)
                        .build(),
                )
            }

            true
        } catch (e: Exception) {
            logger.error("Error creating minio bucket $bucketName", e)
            false
        }

    fun listBuckets(): List<String> = minioClient.listBuckets().map { it.name() }

    fun deleteBucket(bucketName: String): Boolean =
        try {
            minioClient.removeBucket(
                RemoveBucketArgs
                    .builder()
                    .bucket(bucketName)
                    .build(),
            )
            true
        } catch (e: Exception) {
            logger.error("Failed to delete bucket: $bucketName", e)
            false
        }

    companion object {
        private val logger = LoggerFactory.getLogger(MinioHelperService::class.java)
    }
}
