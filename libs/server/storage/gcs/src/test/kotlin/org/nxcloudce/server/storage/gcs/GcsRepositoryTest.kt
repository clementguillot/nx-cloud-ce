package org.nxcloudce.server.storage.gcs

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.fluent.en_GB.toStartWith
import ch.tutteli.atrium.api.verbs.expect
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

@QuarkusTest
@WithTestResource(GcsEmulatorResource::class)
class GcsRepositoryTest {
  @ConfigProperty(name = "gcs.emulator.host")
  lateinit var gcsEndpoint: String

  @ConfigProperty(name = "gcs.emulator.bucket")
  lateinit var gcsBucket: String

  lateinit var storage: Storage
  lateinit var gcsRepository: GcsRepository

  @BeforeEach
  fun setUp() {
    val serviceAccountCredentials = mockk<ServiceAccountCredentials>(relaxed = true)
    every { serviceAccountCredentials.account } returns "Account"
    every { serviceAccountCredentials.sign(any()) } returns byteArrayOf(123456.toByte())
    every { serviceAccountCredentials.universeDomain } returns "googleapis.com"

    storage =
      StorageOptions.newBuilder()
        .setHost(gcsEndpoint)
        .setCredentials(serviceAccountCredentials)
        .build()
        .service
    gcsRepository =
      GcsRepository {
        dispatcher = Dispatchers.IO
        storage = this@GcsRepositoryTest.storage
        bucket = gcsBucket
      }
  }

  @Test
  fun `should presign a GET URL`() =
    runTest {
      // When
      val result = gcsRepository.generateGetUrl("file-path").replace("https://", "http://")

      // Then
      expect(result).toStartWith("$gcsEndpoint/$gcsBucket/file-path?X-Goog-Algorithm=GOOG4-RSA-SHA256")
    }

  @Test
  fun `should presign a PUT URL`() =
    runTest {
      // When
      val result = gcsRepository.generatePutUrl("file-path").replace("https://", "http://")

      // Then
      expect(result).toStartWith("$gcsEndpoint/$gcsBucket/file-path?X-Goog-Algorithm=GOOG4-RSA-SHA256")
    }

  @Test
  fun `should delete a file`() =
    runTest {
      // Given
      storage.createFrom(
        BlobInfo.newBuilder(BlobId.of(gcsBucket, "file-path")).build(),
        ByteArrayInputStream("DUMMY FILE!".toByteArray()),
        Storage.BlobWriteOption.crc32cMatch(),
      )

      val blob = storage.get(gcsBucket, "file-path")
      expect(blob.exists()).toEqual(true)

      // When
      gcsRepository.deleteFile("file-path")

      // Then
      val deletedBlog = storage.get(gcsBucket, "file-path")
      expect(deletedBlog).toEqual(null)
    }
}
