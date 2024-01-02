package org.nxcloudce.api.domain.workspace.usecase

import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.model.Workspace

interface CreateWorkspace {
  suspend fun <T> create(
    request: CreateWorkspaceRequest,
    presenter: (CreateWorkspaceResponse) -> T,
  ): T
}

data class CreateWorkspaceRequest(val orgId: OrganizationId, val name: String)

data class CreateWorkspaceResponse(val workspace: Workspace)
