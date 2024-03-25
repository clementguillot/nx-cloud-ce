package org.nxcloudce.server.domain.workspace.interactor

import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.domain.workspace.exception.OrganizationNotFoundException
import org.nxcloudce.server.domain.workspace.gateway.OrganizationValidationService
import org.nxcloudce.server.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspace
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceResponse

@ApplicationScoped
class CreateWorkspaceImpl(
  private val workspaceGateway: WorkspaceRepository,
  private val orgValidation: OrganizationValidationService,
) : CreateWorkspace {
  override suspend fun <T> create(
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
