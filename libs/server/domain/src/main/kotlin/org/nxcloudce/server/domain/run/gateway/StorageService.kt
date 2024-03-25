package org.nxcloudce.server.domain.run.gateway

import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

interface StorageService {
  suspend fun generateGetUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String

  suspend fun generatePutUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String
}
