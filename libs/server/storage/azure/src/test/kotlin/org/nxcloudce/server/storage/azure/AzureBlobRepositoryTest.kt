package org.nxcloudce.server.storage.azure

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.fluent.en_GB.toStartWith
import ch.tutteli.atrium.api.verbs.expect
import com.azure.core.util.BinaryData
import com.azure.storage.blob.BlobServiceAsyncClient
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@WithTestResource(AzureBlobEmulatorResource::class)
class AzureBlobRepositoryTest {
  @ConfigProperty(name = "azure.blob.endpoint-override")
  lateinit var azureEndpointOverride: String

  lateinit var azureBlogRepository: AzureBlobRepository
  lateinit var blobServiceClient: BlobServiceAsyncClient

  @BeforeEach
  fun setUp() {
    val configuration = AzureBlobConfiguration.readFromConfig("nx-server.storage.azure")
    blobServiceClient = configuration.copy(endpoint = azureEndpointOverride).buildBlobServiceClient()
    azureBlogRepository =
      AzureBlobRepository(
        blobServiceClient,
        configuration.containerName,
      )
  }

  @Test
  fun `should presign a GET URL`() =
    runTest {
      // When
      val result = azureBlogRepository.generateGetUrl("file-path")

      // Then
      expect(result).toStartWith("$azureEndpointOverride/nx-cloud-ce-test/file-path?")
    }

  @Test
  fun `should presign a PUT URL`() =
    runTest {
      // When
      val result = azureBlogRepository.generatePutUrl("file-path")

      // Then
      expect(result).toStartWith("$azureEndpointOverride/nx-cloud-ce-test/file-path?")
    }

  @Test
  fun `should delete a file`() =
    runTest {
      // Given
      val container = blobServiceClient.getBlobContainerAsyncClient("nx-cloud-ce-test")
      val blob = container.getBlobAsyncClient("file-path")
      blob.upload(BinaryData.fromString("DUMMY FILE!")).awaitSingle()

      expect(container.getBlobAsyncClient("file-path").exists().awaitSingle()).toEqual(true)

      // When
      azureBlogRepository.deleteFile("file-path")

      // Then
      expect(container.getBlobAsyncClient("file-path").exists().awaitSingle()).toEqual(false)
    }
}
