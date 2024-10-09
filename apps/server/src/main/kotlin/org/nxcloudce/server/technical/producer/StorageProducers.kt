package org.nxcloudce.server.technical.producer

import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import kotlinx.coroutines.CoroutineDispatcher
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.nxcloudce.server.storage.azure.AzureBlobConfiguration
import org.nxcloudce.server.storage.azure.AzureBlobRepository
import org.nxcloudce.server.storage.core.FileRepository
import org.nxcloudce.server.storage.gcs.GcsConfiguration
import org.nxcloudce.server.storage.gcs.GcsRepository
import org.nxcloudce.server.storage.s3.S3Configuration
import org.nxcloudce.server.storage.s3.S3Repository

@ApplicationScoped
class StorageProducers {
  enum class StorageType(val value: String) {
    S3("s3"),
    GCS("gcs"),
    AZURE("azure"),
    ;

    companion object {
      fun from(value: String): StorageType =
        StorageType.entries.find { it.value == value } ?: throw IllegalArgumentException(("'$value' is not a valid StorageType"))
    }
  }

  @Produces
  @ApplicationScoped
  @Startup
  fun s3repository(
    @ConfigProperty(name = "nx-server.storage.type", defaultValue = "") storageType: String,
    dispatcher: CoroutineDispatcher,
  ): FileRepository =
    when (StorageType.from(storageType)) {
      StorageType.AZURE -> {
        val azureBlobConfiguration = AzureBlobConfiguration.readFromConfig("nx-server.storage.azure")
        AzureBlobRepository(azureBlobConfiguration.buildBlobServiceClient(), azureBlobConfiguration.containerName)
      }
      StorageType.GCS -> {
        val gcsConfiguration = GcsConfiguration.readFromConfig("nx-server.storage.gcs")
        GcsRepository {
          this.gcsConfiguration = gcsConfiguration
          this.dispatcher = dispatcher
        }
      }
      StorageType.S3 -> {
        val s3Configuration = S3Configuration.readFromConfig("nx-server.storage.s3")
        S3Repository {
          this.s3Configuration = s3Configuration
        }
      }
    }
}
