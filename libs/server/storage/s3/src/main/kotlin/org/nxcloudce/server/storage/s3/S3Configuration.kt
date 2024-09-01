package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import org.eclipse.microprofile.config.ConfigProvider
import java.util.*

data class S3Configuration(
  val endpoint: String,
  val region: String,
  val accessKeyId: String,
  val secretAccessKey: String,
  val bucket: String,
  val forcePathStyle: Optional<Boolean>,
) {
  companion object {
    fun readFromConfig(prefix: String): S3Configuration {
      val config = ConfigProvider.getConfig()

      val endpoint = config.getValue("$prefix.endpoint", String::class.java)
      val region = config.getValue("$prefix.region", String::class.java)
      val accessKeyId = config.getValue("$prefix.access-key-id", String::class.java)
      val secretAccessKey = config.getValue("$prefix.secret-access-key", String::class.java)
      val bucket = config.getValue("$prefix.bucket", String::class.java)
      val forcePathStyle = config.getOptionalValue("$prefix.force-path-style", Boolean::class.java)

      return S3Configuration(
        endpoint,
        region,
        accessKeyId,
        secretAccessKey,
        bucket,
        forcePathStyle,
      )
    }
  }

  fun buildS3Client(): S3Client =
    S3Client {
      endpointUrl = Url.parse(this@S3Configuration.endpoint)
      region = this@S3Configuration.region
      forcePathStyle = this@S3Configuration.forcePathStyle.map { it }.orElse(null)
      credentialsProvider =
        StaticCredentialsProvider {
          accessKeyId = this@S3Configuration.accessKeyId
          secretAccessKey = this@S3Configuration.secretAccessKey
        }
    }
}
