package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.nxcloudce.server.storage.gateway.FileRepository

@ApplicationScoped
class S3Producers {
  @Produces
  @ApplicationScoped
  fun s3Client(s3Configuration: S3Configuration): S3Client =
    S3Client {
      endpointUrl = Url.parse(s3Configuration.endpoint())
      region = s3Configuration.region()
      forcePathStyle = s3Configuration.forcePathStyle().map { it }.orElse(null)
      credentialsProvider =
        StaticCredentialsProvider {
          accessKeyId = s3Configuration.accessKeyId()
          secretAccessKey = s3Configuration.secretAccessKey()
        }
    }

  @Produces
  @ApplicationScoped
  fun s3repository(
    s3Client: S3Client,
    s3Configuration: S3Configuration,
  ): FileRepository = S3Repository(s3Client, s3Configuration.bucket())
}
