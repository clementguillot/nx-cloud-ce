package org.graphoenix.server.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.model.Workspace
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceRequest

interface WorkspaceRepository {
  suspend fun create(workspace: CreateWorkspaceRequest): Workspace

  fun create(
    request: CreateOrgAndWorkspaceRequest,
    orgId: OrganizationId,
  ): Uni<Workspace>
}
