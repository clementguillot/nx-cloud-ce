package org.nxcloudce.api.persistence.gateway

import org.nxcloudce.api.domain.run.model.Artifact
import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.ArtifactEntity

fun ArtifactEntity.toDomain() =
  Artifact.Exist(
    id = ArtifactId(artifactId),
    hash = Hash(hash),
    workspaceId = WorkspaceId(workspaceId.toString()),
    get = null,
    put = null,
  )
