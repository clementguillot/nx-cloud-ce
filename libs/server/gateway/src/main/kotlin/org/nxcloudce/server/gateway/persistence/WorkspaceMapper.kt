package org.nxcloudce.server.gateway.persistence

import org.bson.types.ObjectId
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.model.Workspace
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.server.persistence.entity.WorkspaceEntity

fun WorkspaceEntity.toDomain(): Workspace =
  Workspace(
    id = WorkspaceId(id.toString()),
    orgId = OrganizationId(orgId.toString()),
    name = name,
    installationSource = installationSource,
    nxInitDate = nxInitDate,
  )

fun CreateWorkspaceRequest.toEntity(): WorkspaceEntity =
  WorkspaceEntity(
    id = null,
    orgId = ObjectId(orgId.value),
    name = name,
    installationSource = null,
    nxInitDate = null,
  )
