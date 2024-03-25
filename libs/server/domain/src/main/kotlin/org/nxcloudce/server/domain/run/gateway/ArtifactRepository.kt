package org.nxcloudce.server.domain.run.gateway

import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

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
