package org.graphoenix.server.domain.run.gateway

import org.graphoenix.server.domain.run.model.ArtifactId
import org.graphoenix.server.domain.workspace.model.WorkspaceId

interface StorageService {
  suspend fun generateGetUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String

  suspend fun generatePutUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String

  suspend fun deleteArtifact(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  )
}
