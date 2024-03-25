package org.nxcloudce.server.domain.workspace.usecase

import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.model.Workspace

interface CreateWorkspace {
  suspend fun <T> create(
    request: CreateWorkspaceRequest,
    presenter: (CreateWorkspaceResponse) -> T,
  ): T
}

data class CreateWorkspaceRequest(val orgId: OrganizationId, val name: String)

data class CreateWorkspaceResponse(val workspace: Workspace)
