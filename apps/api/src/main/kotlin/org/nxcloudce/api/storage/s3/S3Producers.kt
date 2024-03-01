package org.nxcloudce.api.storage.s3

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.nxcloudce.api.storage.gateway.FileRepository
import org.nxcloudce.api.storage.model.Bucket

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
  fun bucket(s3Configuration: S3Configuration): Bucket = Bucket(s3Configuration.bucket())

  @Produces
  @ApplicationScoped
  fun s3repository(
    s3Client: S3Client,
    bucket: Bucket,
    s3Configuration: S3Configuration,
  ): FileRepository = S3Repository(s3Client, bucket)
}
