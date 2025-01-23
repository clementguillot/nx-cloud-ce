package org.graphoenix.server.domain.workspace.interactor

import org.graphoenix.server.domain.workspace.gateway.AccessTokenRepository
import org.graphoenix.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.graphoenix.server.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import org.graphoenix.server.domain.workspace.usecase.GetWorkspaceAccessTokenResponse

class GetWorkspaceAccessTokenImpl(
  private val accessTokenRepository: AccessTokenRepository,
) : GetWorkspaceAccessToken {
  override suspend operator fun <T> invoke(
    request: GetWorkspaceAccessTokenRequest,
    presenter: (GetWorkspaceAccessTokenResponse) -> T,
  ): T =
    accessTokenRepository.findByEncodedValue(request.encodedAccessToken).run {
      presenter(GetWorkspaceAccessTokenResponse(accessToken = this))
    }
}
