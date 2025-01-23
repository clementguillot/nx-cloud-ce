package org.graphoenix.server.domain.workspace.interactor

import org.graphoenix.server.domain.workspace.exception.OrganizationNotFoundException
import org.graphoenix.server.domain.workspace.gateway.OrganizationValidationService
import org.graphoenix.server.domain.workspace.gateway.WorkspaceRepository
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspace
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceResponse

class CreateWorkspaceImpl(
  private val workspaceGateway: WorkspaceRepository,
  private val orgValidation: OrganizationValidationService,
) : CreateWorkspace {
  override suspend operator fun <T> invoke(
    request: CreateWorkspaceRequest,
    presenter: (CreateWorkspaceResponse) -> T,
  ): T {
    if (!orgValidation.isValidOrgId(request.orgId)) {
      throw OrganizationNotFoundException(request.orgId)
    }
    val workspace = workspaceGateway.create(request)
    return presenter(CreateWorkspaceResponse(workspace))
  }
}
