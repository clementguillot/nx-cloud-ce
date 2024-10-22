package org.nxcloudce.server.gateway.persistence

import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.ArtifactEntity

fun ArtifactEntity.toDomain() =
  Artifact.Exist(
    id = ArtifactId(artifactId),
    hash = Hash(hash),
    workspaceId = WorkspaceId(workspaceId.toString()),
    get = null,
    put = null,
  )
