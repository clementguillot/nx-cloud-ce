package org.nxcloudce.server.storage.core

interface FileRepository {
  suspend fun generateGetUrl(objectPath: String): String

  suspend fun generatePutUrl(objectPath: String): String

  suspend fun deleteFile(objectPath: String)
}
