package org.graphoenix.server.storage.gcs

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "graphoenix-server.storage.gcs")
interface GcsConfiguration {
  fun bucket(): Optional<String>
}
