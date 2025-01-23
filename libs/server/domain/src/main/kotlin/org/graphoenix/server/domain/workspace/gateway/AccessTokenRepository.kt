package org.graphoenix.server.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.graphoenix.server.domain.workspace.model.AccessToken
import org.graphoenix.server.domain.workspace.model.WorkspaceId

interface AccessTokenRepository {
  fun createDefaultAccessToken(workspaceId: WorkspaceId): Uni<AccessToken>

  suspend fun findByEncodedValue(encodedValue: String): AccessToken?
}
