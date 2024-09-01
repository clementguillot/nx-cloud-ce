package org.nxcloudce.server.storage.gcs

import com.google.cloud.NoCredentials
import com.google.cloud.storage.BucketInfo
import com.google.cloud.storage.StorageOptions
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

class GcsEmulatorResource : QuarkusTestResourceLifecycleManager {
  private val bucket = "nx-cloud-ce-test"
  lateinit var container: GenericContainer<*>

  override fun start(): Map<String, String> {
    container =
      GenericContainer("fsouza/fake-gcs-server:1.49.3")
        .withExposedPorts(4443)
        .withCommand("-scheme", "http")
    container.start()

    val emulatorHost = "http://${container.host}:${container.firstMappedPort}"

    updateExternalUrlWithContainerUrl(emulatorHost)
    createBucket(emulatorHost)

    return mapOf(
      "gcs.emulator.host" to emulatorHost,
      "gcs.emulator.bucket" to bucket,
    )
  }

  override fun stop() {
    container.stop()
  }

  private fun updateExternalUrlWithContainerUrl(fakeGcsExternalUrl: String) {
    val modifyExternalUrlRequestUri = "$fakeGcsExternalUrl/_internal/config"
    val updateExternalUrlJson =
      """
      {
        "externalUrl": "$fakeGcsExternalUrl"
      }
      """.trimIndent()

    val req =
      HttpRequest.newBuilder()
        .uri(URI.create(modifyExternalUrlRequestUri))
        .header("Content-Type", "application/json")
        .PUT(BodyPublishers.ofString(updateExternalUrlJson))
        .build()
    val response =
      HttpClient.newBuilder().build()
        .send(req, BodyHandlers.discarding())

    if (response.statusCode() != 200) {
      throw RuntimeException(
        "error updating fake-gcs-server with external url, response status code " + response.statusCode() + " != 200",
      )
    }
  }

  private fun createBucket(endpoint: String) {
    val storage =
      StorageOptions.newBuilder()
        .setHost(endpoint)
        .setProjectId("test-project")
        .setCredentials(NoCredentials.getInstance())
        .build()
        .service
    storage.create(BucketInfo.newBuilder(bucket).build())
  }
}
