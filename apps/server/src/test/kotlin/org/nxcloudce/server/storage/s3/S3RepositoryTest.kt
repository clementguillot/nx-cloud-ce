package org.nxcloudce.server.storage.s3

import aws.sdk.kotlin.services.s3.S3Client
import ch.tutteli.atrium.api.fluent.en_GB.toStartWith
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class S3RepositoryTest {
  @Inject
  lateinit var s3Client: S3Client

  lateinit var s3Repository: S3Repository

  @BeforeEach
  fun setUp() {
    s3Repository = S3Repository(s3Client, "nx-cloud-ce-test")
  }

  @Test
  fun `should presign a GET URL`() =
    runTest {
      // When
      val result = s3Repository.generateGetUrl("file-path")

      // Then
      expect(result).toStartWith("http://localhost:4566/nx-cloud-ce-test/file-path?x-id=GetObject")
    }

  @Test
  fun `should presign a PUT URL`() =
    runTest {
      // When
      val result = s3Repository.generatePutUrl("file-path")

      // Then
      expect(result).toStartWith("http://localhost:4566/nx-cloud-ce-test/file-path?x-id=PutObject")
    }
}
