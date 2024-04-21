package org.nxcloudce.server.storage.gateway

interface FileRepository {
  suspend fun generateGetUrl(objectPath: String): String

  suspend fun generatePutUrl(objectPath: String): String

  suspend fun deleteFile(objectPath: String)
}
