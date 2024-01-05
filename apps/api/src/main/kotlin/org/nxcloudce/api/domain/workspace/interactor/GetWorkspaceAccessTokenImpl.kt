import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessTokenResponse

@ApplicationScoped
class GetWorkspaceAccessTokenImpl(private val accessTokenRepository: AccessTokenRepository) : GetWorkspaceAccessToken {
  override suspend fun <T> getAccessToken(
    request: GetWorkspaceAccessTokenRequest,
    presenter: (GetWorkspaceAccessTokenResponse) -> T,
  ): T =
    accessTokenRepository.findByEncodedValue(request.encodedAccessToken).run {
      presenter(GetWorkspaceAccessTokenResponse(accessToken = this))
    }
}
