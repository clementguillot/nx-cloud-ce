package org.nxcloudce.server.storage.azure

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.fluent.en_GB.toStartWith
import ch.tutteli.atrium.api.verbs.expect
import com.azure.core.util.BinaryData
import com.azure.storage.blob.BlobServiceClient
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class AzureBlobRepositoryTest {
  @Inject
  lateinit var azureBlogRepository: AzureBlobRepository

  @Inject
  lateinit var blobServiceClient: BlobServiceClient

  @Inject
  lateinit var azureBlobConfiguration: AzureBlobConfiguration

  @ConfigProperty(name = "quarkus.azure.storage.blob.connection-string")
  lateinit var azureConnectionString: String
  lateinit var azureEndpoint: String

  @BeforeEach
  fun setUp() {
    try {
      blobServiceClient.createBlobContainer(azureBlobConfiguration.containerName().get())
    } catch (_: Exception) {
    }
    azureEndpoint = "BlobEndpoint=([^;]+);".toRegex().find(azureConnectionString)?.groupValues?.get(1)!!
  }

  @Test
  fun `should presign a GET URL`() =
    runTest {
      // When
      val result = azureBlogRepository.generateGetUrl("file-path")

      // Then
      expect(result).toStartWith("$azureEndpoint/nx-cloud-ce-test/file-path?")
    }

  @Test
  fun `should presign a PUT URL`() =
    runTest {
      // When
      val result = azureBlogRepository.generatePutUrl("file-path")

      // Then
      expect(result).toStartWith("$azureEndpoint/nx-cloud-ce-test/file-path?")
    }

  @Test
  fun `should delete a file`() =
    runTest {
      // Given
      val container = blobServiceClient.getBlobContainerClient(azureBlobConfiguration.containerName().get())
      val blob = container.getBlobClient("file-path")
      blob.upload(BinaryData.fromString("DUMMY FILE!"))

      expect(container.getBlobClient("file-path").exists()).toEqual(true)

      // When
      azureBlogRepository.deleteFile("file-path")

      // Then
      expect(container.getBlobClient("file-path").exists()).toEqual(false)
    }
}
