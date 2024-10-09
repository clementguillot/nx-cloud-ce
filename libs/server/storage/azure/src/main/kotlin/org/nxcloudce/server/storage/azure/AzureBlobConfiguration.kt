package org.nxcloudce.server.storage.azure

import com.azure.storage.blob.BlobServiceAsyncClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.common.StorageSharedKeyCredential
import org.eclipse.microprofile.config.ConfigProvider

data class AzureBlobConfiguration(
  val accountName: String,
  val accountKey: String,
  val endpoint: String,
  val containerName: String,
) {
  companion object {
    fun readFromConfig(prefix: String): AzureBlobConfiguration {
      val config = ConfigProvider.getConfig()

      val accountName = config.getValue("$prefix.account-name", String::class.java)
      val accountKey = config.getValue("$prefix.account-key", String::class.java)
      val endpoint = config.getValue("$prefix.endpoint", String::class.java)
      val containerName = config.getValue("$prefix.container-name", String::class.java)

      return AzureBlobConfiguration(accountName, accountKey, endpoint, containerName)
    }
  }

  fun buildBlobServiceClient(): BlobServiceAsyncClient =
    BlobServiceClientBuilder().endpoint(endpoint)
      .credential(StorageSharedKeyCredential(accountName, accountKey))
      .buildAsyncClient()
}
