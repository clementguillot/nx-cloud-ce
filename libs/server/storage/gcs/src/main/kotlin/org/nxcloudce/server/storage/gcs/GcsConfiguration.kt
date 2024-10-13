package org.nxcloudce.server.storage.gcs

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "nx-server.storage.gcs")
interface GcsConfiguration {
  fun bucket(): Optional<String>
}
