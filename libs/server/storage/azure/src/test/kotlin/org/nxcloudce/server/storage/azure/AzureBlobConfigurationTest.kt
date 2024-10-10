package org.nxcloudce.server.storage.azure

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toBeAnInstanceOf
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import com.azure.storage.blob.BlobServiceAsyncClient
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
@WithTestResource(AzureBlobEmulatorResource::class)
class AzureBlobConfigurationTest {
  @Test
  fun `should return Azure Blob Configuration object from App Config`() {
    val azureBlobConfiguration = AzureBlobConfiguration.readFromConfig("nx-server.storage.azure")

    expect(azureBlobConfiguration) {
      its { accountName }.toEqual("devstoreaccount1")
      its { endpoint }.toEqual("https://dummy")
      its { containerName }.toEqual("nx-cloud-ce-test")
    }

    val client = azureBlobConfiguration.buildBlobServiceClient()
    expect(client).toBeAnInstanceOf<BlobServiceAsyncClient>()
  }
}
