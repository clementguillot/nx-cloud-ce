package org.nxcloudce.server.storage.gcs

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toBeAnInstanceOf
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import com.google.cloud.storage.Storage
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
class GcsConfigurationTest {
  @Test
  fun `should return GCS Configuration object from App Config`() {
    val gcsConfiguration = GcsConfiguration.readFromConfig("nx-server.storage.gcs")

    expect(gcsConfiguration) {
      its { projectId }.toEqual("nx-cloud-ce")
      its { bucket }.toEqual(
        "nx-cloud-ce-test",
      )
    }

    val storage = gcsConfiguration.buildStorage()
    expect(storage).toBeAnInstanceOf<Storage>()
  }
}
