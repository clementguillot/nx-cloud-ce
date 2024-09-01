package org.nxcloudce.server.storage.gcs

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.eclipse.microprofile.config.ConfigProvider
import java.io.ByteArrayInputStream

data class GcsConfiguration(
  val projectId: String,
  val credentials: String,
  val bucket: String,
) {
  companion object {
    fun readFromConfig(prefix: String): GcsConfiguration {
      val config = ConfigProvider.getConfig()

      val projectId = config.getValue("$prefix.project-id", String::class.java)
      val credentials = config.getValue("$prefix.credentials", String::class.java)
      val bucket = config.getValue("$prefix.bucket", String::class.java)

      return GcsConfiguration(
        projectId,
        credentials,
        bucket,
      )
    }
  }

  fun buildStorage(): Storage {
    val credentials = GoogleCredentials.fromStream(ByteArrayInputStream(credentials.toByteArray()))
    return StorageOptions.newBuilder()
      .setProjectId(projectId)
      .setCredentials(credentials)
      .build()
      .service
  }
}
