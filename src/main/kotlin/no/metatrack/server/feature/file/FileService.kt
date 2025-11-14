package no.metatrack.server.feature.file

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class FileService(
    private val minioClient: MinioClient,
) {
    fun uploadFile(
        bucketName: String,
        objectName: String,
        fileStream: InputStream,
        contentType: String,
    ) {
        val exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())

        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }

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
