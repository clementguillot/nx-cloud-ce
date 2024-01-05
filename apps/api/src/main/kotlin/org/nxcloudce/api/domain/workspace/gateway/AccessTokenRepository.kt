package org.nxcloudce.api.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.nxcloudce.api.domain.workspace.model.AccessToken
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

interface AccessTokenRepository {
  fun createDefaultAccessToken(workspaceId: WorkspaceId): Uni<AccessToken>

  suspend fun findByEncodedValue(encodedValue: String): AccessToken?
}
