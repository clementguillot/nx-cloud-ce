package org.nxcloudce.api.domain.workspace.usecase

import io.smallrye.mutiny.Uni
import org.nxcloudce.api.domain.workspace.model.AccessToken
import org.nxcloudce.api.domain.workspace.model.Workspace

interface CreateOrgAndWorkspace {
  fun <T> create(
    request: CreateOrgAndWorkspaceRequest,
    presenter: (Uni<CreateOrgAndWorkspaceResponse>) -> Uni<T>,
  ): Uni<T>
}

data class CreateOrgAndWorkspaceRequest(val workspaceName: String, val installationSource: String)

data class CreateOrgAndWorkspaceResponse(val workspace: Workspace, val accessToken: AccessToken)