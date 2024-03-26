package org.nxcloudce.server.domain.workspace.usecase

import org.nxcloudce.server.domain.UseCase
import org.nxcloudce.server.domain.workspace.model.AccessToken

interface GetWorkspaceAccessToken : UseCase<GetWorkspaceAccessTokenRequest, GetWorkspaceAccessTokenResponse>

data class GetWorkspaceAccessTokenRequest(val encodedAccessToken: String)

data class GetWorkspaceAccessTokenResponse(val accessToken: AccessToken?)
