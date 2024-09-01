package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import org.nxcloudce.server.storage.core.FileRepository
import kotlin.time.Duration.Companion.hours

class S3Repository private constructor(
  private val s3Client: S3Client,
  private val bucket: String,
) : FileRepository {
  companion object {
    private val presignExpiration = 1.hours

    operator fun invoke(block: Builder.() -> Unit): S3Repository {
      val builder = Builder()
      block(builder)
      return builder.build()
    }
  }

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

  class Builder {
    var s3Configuration: S3Configuration? = null
    var s3Client: S3Client? = null
    var bucket: String? = null

    fun build(): S3Repository =
      s3Configuration?.let {
        S3Repository(it.buildS3Client(), it.bucket)
      } ?: run {
        requireNotNull(s3Client)
        requireNotNull(bucket)
        S3Repository(s3Client!!, bucket!!)
      }
  }
}
