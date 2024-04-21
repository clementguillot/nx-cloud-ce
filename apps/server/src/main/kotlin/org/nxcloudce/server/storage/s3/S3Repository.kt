package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import org.nxcloudce.server.storage.gateway.FileRepository
import kotlin.time.Duration.Companion.hours

class S3Repository(
  private val s3Client: S3Client,
  private val bucketName: String,
) : FileRepository {
  companion object {
    private val presignExpiration = 1.hours
  }

  override suspend fun generateGetUrl(objectPath: String): String {
    val getRequest =
      GetObjectRequest {
        bucket = this@S3Repository.bucketName
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
        bucket = this@S3Repository.bucketName
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
        bucket = this@S3Repository.bucketName
        key = objectPath
      }
    s3Client.deleteObject(deleteRequest)
  }
}
