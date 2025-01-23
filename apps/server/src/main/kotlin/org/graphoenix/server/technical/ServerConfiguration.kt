package org.graphoenix.server.technical

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "graphoenix-server.configuration")
interface ServerConfiguration {
  /** Domain used to access to the application. */
  fun applicationUrl(): String

  /** Version of the `nx-cloud-client-bundle` included in the application. */
  fun clientBundleVersion(): String

  /** Static path to access `nx-cloud-client-bundle`. Must not contain domain! */
  fun clientBundlePath(): String
}
