package org.graphoenix.server.domain.workspace.usecase

import org.graphoenix.server.domain.UseCase
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.model.Workspace

interface CreateWorkspace : UseCase<CreateWorkspaceRequest, CreateWorkspaceResponse>

data class CreateWorkspaceRequest(
  val orgId: OrganizationId,
  val name: String,
)

data class CreateWorkspaceResponse(
  val workspace: Workspace,
)
