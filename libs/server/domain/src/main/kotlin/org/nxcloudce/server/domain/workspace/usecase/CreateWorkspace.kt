package org.nxcloudce.server.domain.workspace.usecase

import org.nxcloudce.server.domain.UseCase
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.model.Workspace

interface CreateWorkspace : UseCase<CreateWorkspaceRequest, CreateWorkspaceResponse>

data class CreateWorkspaceRequest(val orgId: OrganizationId, val name: String)

data class CreateWorkspaceResponse(val workspace: Workspace)
