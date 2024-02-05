package org.nxcloudce.api.domain.run.gateway

import org.nxcloudce.api.domain.run.model.Artifact
import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

interface ArtifactRepository {
  suspend fun findByHash(
    hashes: Collection<Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.Exist>

  suspend fun createWithHash(
    hashes: Collection<Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.New>

  suspend fun createRemoteArtifacts(
    artifact: Map<ArtifactId, Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.Exist>
}
