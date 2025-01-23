package org.graphoenix.server.gateway.persistence

import org.graphoenix.server.domain.run.model.Artifact
import org.graphoenix.server.domain.run.model.ArtifactId
import org.graphoenix.server.domain.run.model.Hash
import org.graphoenix.server.domain.workspace.model.WorkspaceId
import org.graphoenix.server.persistence.entity.ArtifactEntity

fun ArtifactEntity.toDomain() =
  Artifact.Exist(
    id = ArtifactId(artifactId),
    hash = Hash(hash),
    workspaceId = WorkspaceId(workspaceId.toString()),
    get = null,
    put = null,
  )
