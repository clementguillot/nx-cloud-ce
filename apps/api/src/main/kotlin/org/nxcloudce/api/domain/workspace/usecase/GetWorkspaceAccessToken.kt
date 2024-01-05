package org.nxcloudce.api.domain.workspace.usecase

import org.nxcloudce.api.domain.workspace.model.AccessToken

interface GetWorkspaceAccessToken {
  suspend fun <T> getAccessToken(
    request: GetWorkspaceAccessTokenRequest,
    presenter: (GetWorkspaceAccessTokenResponse) -> T,
  ): T
}

data class GetWorkspaceAccessTokenRequest(val encodedAccessToken: String)

data class GetWorkspaceAccessTokenResponse(val accessToken: AccessToken?)
