package org.nxcloudce.api.storage.gateway

interface FileRepository {
  suspend fun generateGetUrl(objectPath: String): String

  suspend fun generatePutUrl(objectPath: String): String
}
