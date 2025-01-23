package org.graphoenix.server.domain.workspace.interactor

import io.quarkus.mongodb.panache.common.reactive.Panache.withTransaction
import io.smallrye.mutiny.Uni
import org.graphoenix.server.domain.workspace.gateway.AccessTokenRepository
import org.graphoenix.server.domain.workspace.gateway.OrganizationCreationService
import org.graphoenix.server.domain.workspace.gateway.WorkspaceRepository
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspace
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspaceResponse

class CreateOrgAndWorkspaceImpl(
  private val workspaceRepository: WorkspaceRepository,
  private val orgCreation: OrganizationCreationService,
  private val accessTokenRepository: AccessTokenRepository,
) : CreateOrgAndWorkspace {
  override operator fun <T> invoke(
    request: CreateOrgAndWorkspaceRequest,
    presenter: (Uni<CreateOrgAndWorkspaceResponse>) -> Uni<T>,
  ): Uni<T> =
    presenter(
      withTransaction {
        orgCreation
          .createOrg(request.workspaceName)
          .onItem()
          .transform { org ->
            workspaceRepository
              .create(request, org.id)
              .onItem()
              .transform { workspace ->
                accessTokenRepository.createDefaultAccessToken(workspace.id).onItem().transform { token ->
                  Pair(workspace, token)
                }
              }
          }.flatMap { firstLevelUni -> firstLevelUni.flatMap { secondLevelUni -> secondLevelUni } }
          .onItem()
          .transform { CreateOrgAndWorkspaceResponse(it.first, it.second) }
      },
    )
}
