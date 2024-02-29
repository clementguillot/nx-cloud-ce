package org.nxcloudce.api.persistence.gateway

import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.persistence.entity.WorkspaceEntity

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
