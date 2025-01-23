package org.graphoenix.server.domain.run.gateway

import org.graphoenix.server.domain.run.model.Artifact
import org.graphoenix.server.domain.run.model.ArtifactId
import org.graphoenix.server.domain.run.model.Hash
import org.graphoenix.server.domain.workspace.model.WorkspaceId

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

  suspend fun delete(artifactId: ArtifactId): Boolean
}
