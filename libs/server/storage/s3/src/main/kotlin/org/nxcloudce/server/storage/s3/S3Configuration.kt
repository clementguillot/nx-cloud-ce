package org.nxcloudce.server.storage.s3

import io.quarkus.arc.lookup.LookupIfProperty
import io.smallrye.config.ConfigMapping
import java.util.*

@LookupIfProperty(name = "nx-server.storage.type", stringValue = "s3")
@ConfigMapping(prefix = "nx-server.storage.s3")
interface S3Configuration {
  fun endpoint(): Optional<String>

  fun region(): Optional<String>

  fun accessKeyId(): Optional<String>

  fun secretAccessKey(): Optional<String>

  fun bucket(): Optional<String>

  fun forcePathStyle(): Optional<Boolean>
}
