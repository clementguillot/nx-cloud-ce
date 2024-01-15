package org.nxcloudce.api.storage.s3

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nx-server.storage.s3")
interface S3Configuration {
  fun endpoint(): String

  fun region(): String

  fun accessKeyId(): String

  fun secretAccessKey(): String

  fun bucket(): String

  fun forcePathStyle(): Boolean?
}
