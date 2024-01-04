package org.nxcloudce.api.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest

interface WorkspaceRepository {
  suspend fun create(workspace: CreateWorkspaceRequest): Workspace

  fun create(
    name: String,
    orgId: OrganizationId,
    installationSource: String,
  ): Uni<Workspace>
}
