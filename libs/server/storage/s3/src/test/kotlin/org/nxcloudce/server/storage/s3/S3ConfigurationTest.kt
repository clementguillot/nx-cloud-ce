package org.nxcloudce.server.storage.s3

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
class S3ConfigurationTest {
  @Test
  fun `should return S3 Configuration object from App Config`() {
    val s3Config = S3Configuration.readFromConfig("nx-server.storage.s3")

    expect(s3Config) {
      its { endpoint }.toEqual("http://localhost:4566")
      its { region }.toEqual("us-east-1")
      its { accessKeyId }.toEqual("test-key")
      its { secretAccessKey }.toEqual("test-secret")
      its { bucket }.toEqual("nx-cloud-ce-test")
      its { forcePathStyle.get() }.toEqual(true)
    }
  }
}
