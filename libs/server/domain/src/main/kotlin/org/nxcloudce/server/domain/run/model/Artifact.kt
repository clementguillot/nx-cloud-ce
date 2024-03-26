package org.nxcloudce.server.domain.run.model

import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import java.util.*

@JvmInline value class ArtifactId(val value: String = UUID.randomUUID().toString())

sealed class Artifact {
  abstract val id: ArtifactId
  abstract val hash: Hash
  abstract val workspaceId: WorkspaceId
  abstract var put: String?

  data class Exist(
    override val id: ArtifactId,
    override val hash: Hash,
    override val workspaceId: WorkspaceId,
    override var put: String?,
    var get: String?,
  ) : Artifact() {
    suspend fun setGetUrl(storageService: StorageService) {
      get = storageService.generateGetUrl(id, workspaceId)
    }
  }

  data class New(
    override val id: ArtifactId,
    override val hash: Hash,
    override val workspaceId: WorkspaceId,
    override var put: String?,
  ) : Artifact()

  suspend fun setPutUrl(storageService: StorageService) {
    put = storageService.generatePutUrl(id, workspaceId)
  }
}
