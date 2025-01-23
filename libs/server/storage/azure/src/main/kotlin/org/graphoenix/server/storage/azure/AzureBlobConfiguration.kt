package org.graphoenix.server.storage.azure

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "graphoenix-server.storage.azure")
interface AzureBlobConfiguration {
  fun containerName(): Optional<String>
}
