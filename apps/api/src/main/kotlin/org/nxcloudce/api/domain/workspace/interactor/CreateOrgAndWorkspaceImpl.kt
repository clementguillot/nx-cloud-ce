package org.nxcloudce.api.domain.workspace.interactor

import io.quarkus.mongodb.panache.common.reactive.Panache.withTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.api.domain.workspace.gateway.OrganizationCreationService
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspace
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspaceResponse

@ApplicationScoped
class CreateOrgAndWorkspaceImpl(
  private val workspaceRepository: WorkspaceRepository,
  private val orgCreation: OrganizationCreationService,
  private val accessTokenRepository: AccessTokenRepository,
) : CreateOrgAndWorkspace {
  override fun <T> create(
    request: CreateOrgAndWorkspaceRequest,
    presenter: (Uni<CreateOrgAndWorkspaceResponse>) -> Uni<T>,
  ): Uni<T> {
    return presenter(
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
          }
          .flatMap { firstLevelUni -> firstLevelUni.flatMap { secondLevelUni -> secondLevelUni } }
          .onItem()
          .transform { CreateOrgAndWorkspaceResponse(it.first, it.second) }
      },
    )
  }
}
