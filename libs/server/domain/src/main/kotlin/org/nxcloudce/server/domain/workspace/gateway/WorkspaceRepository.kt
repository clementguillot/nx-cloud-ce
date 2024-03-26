package org.nxcloudce.server.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.model.Workspace
import org.nxcloudce.server.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceRequest

interface WorkspaceRepository {
  suspend fun create(workspace: CreateWorkspaceRequest): Workspace

  fun create(
    request: CreateOrgAndWorkspaceRequest,
    orgId: OrganizationId,
  ): Uni<Workspace>
}
