package org.nxcloudce.server.storage.azure

import com.azure.storage.blob.BlobAsyncClient
import com.azure.storage.blob.BlobServiceAsyncClient
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import io.quarkus.arc.lookup.LookupIfProperty
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.reactive.awaitSingle
import org.nxcloudce.server.storage.core.FileRepository
import java.time.OffsetDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

@LookupIfProperty(name = "nx-server.storage.type", stringValue = "azure")
@ApplicationScoped
class AzureBlobRepository(
  private val blobServiceClient: BlobServiceAsyncClient,
  azureBlobConfiguration: AzureBlobConfiguration,
) : FileRepository {
  companion object {
    private val presignExpiration = 1.hours
  }

  init {
    require(azureBlobConfiguration.containerName().isPresent)
  }

  private val containerName = azureBlobConfiguration.containerName().get()

  override suspend fun generateGetUrl(objectPath: String): String {
    val containerClient = blobServiceClient.getBlobContainerAsyncClient(containerName)
    val blobClient = containerClient.getBlobAsyncClient(objectPath)

    val sasPermission = BlobSasPermission().setReadPermission(true)
    val sasToken = buildSasToken(blobClient, sasPermission)

    return "${blobClient.blobUrl}?$sasToken"
  }

  override suspend fun generatePutUrl(objectPath: String): String {
    val containerClient = blobServiceClient.getBlobContainerAsyncClient(containerName)
    val blobClient = containerClient.getBlobAsyncClient(objectPath)

    val sasPermission = BlobSasPermission().setWritePermission(true)
    val sasToken = buildSasToken(blobClient, sasPermission)

    return "${blobClient.blobUrl}?$sasToken"
  }

  override suspend fun deleteFile(objectPath: String) {
    val containerClient = blobServiceClient.getBlobContainerAsyncClient(containerName)
    val blobClient = containerClient.getBlobAsyncClient(objectPath)

    blobClient.deleteIfExists().awaitSingle()
  }

  private fun buildSasToken(
    blobClient: BlobAsyncClient,
    sasPermission: BlobSasPermission,
  ): String =
    BlobServiceSasSignatureValues(
      OffsetDateTime.now().plus(presignExpiration.toJavaDuration()),
      sasPermission,
    ).setStartTime(OffsetDateTime.now())
      .let { sasValue ->
        blobClient.generateSas(sasValue)
      }
}
