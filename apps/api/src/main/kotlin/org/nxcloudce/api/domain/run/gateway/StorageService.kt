package org.nxcloudce.api.domain.run.gateway

import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

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
