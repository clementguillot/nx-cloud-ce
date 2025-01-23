package org.graphoenix.server.storage.s3

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "graphoenix-server.storage.s3")
interface S3Configuration {
  fun endpoint(): Optional<String>

  fun region(): Optional<String>

  fun accessKeyId(): Optional<String>

  fun secretAccessKey(): Optional<String>

  fun bucket(): Optional<String>

  fun forcePathStyle(): Optional<Boolean>
}
