package org.nxcloudce.server.domain.workspace.interactor

import org.nxcloudce.server.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenResponse

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
