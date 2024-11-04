package org.nxcloudce.server.domain.workspace.usecase

import io.smallrye.mutiny.Uni
import org.nxcloudce.server.domain.workspace.model.AccessToken
import org.nxcloudce.server.domain.workspace.model.Workspace
import java.time.LocalDateTime

interface CreateOrgAndWorkspace {
  operator fun <T> invoke(
    request: CreateOrgAndWorkspaceRequest,
    presenter: (Uni<CreateOrgAndWorkspaceResponse>) -> Uni<T>,
  ): Uni<T>
}

data class CreateOrgAndWorkspaceRequest(
  val workspaceName: String,
  val installationSource: String,
  val nxInitDate: LocalDateTime?,
)

data class CreateOrgAndWorkspaceResponse(
  val workspace: Workspace,
  val accessToken: AccessToken,
)
