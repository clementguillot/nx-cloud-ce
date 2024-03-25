package org.nxcloudce.server.presentation.infrastructure.filter

import io.quarkus.runtime.Startup
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.SecurityContext
import org.jboss.resteasy.reactive.server.ServerRequestFilter
import org.nxcloudce.server.domain.workspace.model.AccessToken
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import java.security.Principal

@Startup
class SecurityContextFilter(private val getWorkspaceAccessToken: GetWorkspaceAccessToken) {
  @ServerRequestFilter(preMatching = true)
  suspend fun preMatchingFilter(requestContext: ContainerRequestContext) {
    val apiKey = requestContext.headers.getFirst("authorization")
    if (apiKey.isNullOrEmpty()) {
      return
    }

    getWorkspaceAccessToken.getAccessToken(GetWorkspaceAccessTokenRequest(encodedAccessToken = apiKey)) { response ->
      if (response.accessToken == null) {
        return@getAccessToken
      }
      requestContext.securityContext = buildSecurityContext(response.accessToken)
    }
  }

  private fun buildSecurityContext(accessToken: AccessToken) =
    object : SecurityContext {
      val accessLevel = accessToken.accessLevel.value

      override fun getUserPrincipal(): Principal = Principal { accessToken.workspaceId.value }

      override fun isUserInRole(role: String) = accessLevel == role

      override fun isSecure() = true

      override fun getAuthenticationScheme() = "api-key"
    }
}
