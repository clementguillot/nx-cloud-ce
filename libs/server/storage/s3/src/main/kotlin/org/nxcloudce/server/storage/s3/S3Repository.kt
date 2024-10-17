package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import aws.smithy.kotlin.runtime.net.url.Url
import io.quarkus.arc.lookup.LookupIfProperty
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.storage.core.FileRepository
import kotlin.time.Duration.Companion.hours

@LookupIfProperty(name = "nx-server.storage.type", stringValue = "s3")
@ApplicationScoped
class S3Repository(
  s3Configuration: S3Configuration,
) : FileRepository {
  companion object {
    private val presignExpiration = 1.hours
  }

  init {
    require(s3Configuration.endpoint().isPresent)
    require(s3Configuration.region().isPresent)
    require(s3Configuration.accessKeyId().isPresent)
    require(s3Configuration.secretAccessKey().isPresent)
    require(s3Configuration.bucket().isPresent)
  }

  private val s3Client =
    S3Client {
      endpointUrl = Url.parse(s3Configuration.endpoint().get())
      region = s3Configuration.region().get()
      forcePathStyle = s3Configuration.forcePathStyle().map { it }.orElse(null)
      credentialsProvider =
        StaticCredentialsProvider {
          accessKeyId = s3Configuration.accessKeyId().get()
          secretAccessKey = s3Configuration.secretAccessKey().get()
        }
    }
  private val bucket = s3Configuration.bucket().get()

  override suspend fun generateGetUrl(objectPath: String): String {
    val getRequest =
      GetObjectRequest {
        bucket = this@S3Repository.bucket
        key = objectPath
      }
    return s3Client
      .presignGetObject(getRequest, presignExpiration)
      .url
      .toString()
  }

  override suspend fun generatePutUrl(objectPath: String): String {
    val putRequest =
      PutObjectRequest {
        bucket = this@S3Repository.bucket
        key = objectPath
      }
    return s3Client
      .presignPutObject(putRequest, presignExpiration)
      .url
      .toString()
  }

  override suspend fun deleteFile(objectPath: String) {
    val deleteRequest =
      DeleteObjectRequest {
        bucket = this@S3Repository.bucket
        key = objectPath
      }
    s3Client.deleteObject(deleteRequest)
  }
}
