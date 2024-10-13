package org.nxcloudce.server.storage.azure

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "nx-server.storage.azure")
interface AzureBlobConfiguration {
  fun containerName(): Optional<String>
}
