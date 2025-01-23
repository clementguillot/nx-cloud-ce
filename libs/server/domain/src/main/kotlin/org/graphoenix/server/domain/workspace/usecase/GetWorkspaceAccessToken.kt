package org.graphoenix.server.domain.workspace.usecase

import org.graphoenix.server.domain.UseCase
import org.graphoenix.server.domain.workspace.model.AccessToken

interface GetWorkspaceAccessToken : UseCase<GetWorkspaceAccessTokenRequest, GetWorkspaceAccessTokenResponse>

data class GetWorkspaceAccessTokenRequest(
  val encodedAccessToken: String,
)

data class GetWorkspaceAccessTokenResponse(
  val accessToken: AccessToken?,
)
