package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.NoSuchKey
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import ch.tutteli.atrium.api.fluent.en_GB.notToEqualNull
import ch.tutteli.atrium.api.fluent.en_GB.toStartWith
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

@QuarkusTest
class S3RepositoryTest {
  @ConfigProperty(name = "quarkus.s3.endpoint-override")
  lateinit var s3EndpointOverride: String
  lateinit var s3Client: S3Client
  lateinit var s3Repository: S3Repository

  @BeforeEach
  fun setUp() {
    val s3Configuration =
      S3Configuration(
        endpoint = s3EndpointOverride,
        region = "us-east-1",
        accessKeyId = "test-key",
        secretAccessKey = "test-secret",
        bucket = "nx-cloud-ce-test",
        forcePathStyle = Optional.of(true),
      )
    s3Client = s3Configuration.buildS3Client()
    s3Repository =
      S3Repository {
        s3Client = s3Configuration.buildS3Client()
        bucket = s3Configuration.bucket
      }
  }

  @Test
  fun `should presign a GET URL`() =
    runTest {
      // When
      val result = s3Repository.generateGetUrl("file-path")

      // Then
      expect(result).toStartWith("$s3EndpointOverride/nx-cloud-ce-test/file-path?x-id=GetObject")
    }

  @Test
  fun `should presign a PUT URL`() =
    runTest {
      // When
      val result = s3Repository.generatePutUrl("file-path")

      // Then
      expect(result).toStartWith("$s3EndpointOverride/nx-cloud-ce-test/file-path?x-id=PutObject")
    }

  @Test
  fun `should delete a file`() =
    runTest {
      // Given
      val dummyRequest =
        PutObjectRequest {
          bucket = "nx-cloud-ce-test"
          key = "file-path"
          body = ByteStream.fromString("DUMMY FILE!")
        }
      s3Client.putObject(dummyRequest)
      val getRequest =
        GetObjectRequest {
          bucket = "nx-cloud-ce-test"
          key = "file-path"
        }
      val beforeResponse = s3Client.getObject(getRequest) { it }
      expect(beforeResponse.contentLength).notToEqualNull()

      // When
      s3Repository.deleteFile("file-path")

      // Then
      expect {
        runBlocking {
          s3Client.getObject(getRequest) {}
        }
      }.toThrow<NoSuchKey>()
    }
}
