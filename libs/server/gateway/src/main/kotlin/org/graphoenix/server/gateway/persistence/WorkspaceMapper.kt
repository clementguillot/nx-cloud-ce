package org.graphoenix.server.gateway.persistence

import org.bson.types.ObjectId
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.model.Workspace
import org.graphoenix.server.domain.workspace.model.WorkspaceId
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.graphoenix.server.persistence.entity.WorkspaceEntity

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
