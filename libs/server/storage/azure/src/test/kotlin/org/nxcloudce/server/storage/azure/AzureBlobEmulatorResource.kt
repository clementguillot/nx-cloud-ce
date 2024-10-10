package org.nxcloudce.server.storage.azure

import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.common.StorageSharedKeyCredential
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer

class AzureBlobEmulatorResource : QuarkusTestResourceLifecycleManager {
  lateinit var container: GenericContainer<*>

  override fun start(): Map<String, String> {
    container =
      GenericContainer("mcr.microsoft.com/azure-storage/azurite:3.30.0")
        .withExposedPorts(10000)
        .withCommand(
          "azurite",
          "--skipApiVersionCheck",
          "--blobHost",
          "0.0.0.0",
        )
    container.start()

    val emulatorHost = "http://${container.host}:${container.firstMappedPort}/devstoreaccount1"
    val configuration = AzureBlobConfiguration.readFromConfig("nx-server.storage.azure")

    createBucket(emulatorHost, configuration)

    return mapOf(
      "azure.blob.endpoint-override" to emulatorHost,
    )
  }

  override fun stop() {
    container.stop()
  }

  private fun createBucket(
    endpoint: String,
    configuration: AzureBlobConfiguration,
  ) {
    val client =
      BlobServiceClientBuilder().endpoint(endpoint)
        .credential(StorageSharedKeyCredential(configuration.accountName, configuration.accountKey))
        .buildClient()

    client.createBlobContainer(configuration.containerName)
  }
}
