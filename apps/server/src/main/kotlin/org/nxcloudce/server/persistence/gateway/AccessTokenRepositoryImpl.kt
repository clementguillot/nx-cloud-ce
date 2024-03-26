package org.nxcloudce.server.persistence.gateway

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.server.domain.workspace.model.AccessLevel
import org.nxcloudce.server.domain.workspace.model.AccessToken
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.AccessTokenEntity
import org.nxcloudce.server.persistence.repository.AccessTokenPanacheRepository
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@ApplicationScoped
class AccessTokenRepositoryImpl(
  private val tokenPanacheRepository: AccessTokenPanacheRepository,
) : AccessTokenRepository {
  override fun createDefaultAccessToken(workspaceId: WorkspaceId): Uni<AccessToken> {
    val entity = buildDefaultAccessToken(workspaceId)
    return tokenPanacheRepository.persist(entity).onItem().transform { it.toDomain() }
  }

  override suspend fun findByEncodedValue(encodedValue: String): AccessToken? {
    val entity = tokenPanacheRepository.find(AccessTokenEntity::encodedValue.name, encodedValue).firstResult().awaitSuspending()
    return entity?.toDomain()
  }

  @OptIn(ExperimentalEncodingApi::class)
  private fun buildDefaultAccessToken(
    workspaceId: WorkspaceId,
    publicId: UUID = UUID.randomUUID(),
    accessLevel: AccessLevel = AccessLevel.READ_WRITE,
  ) = AccessTokenEntity(
    id = null,
    name = "default",
    publicId = publicId.toString(),
    accessLevel = accessLevel.value,
    workspaceId = ObjectId(workspaceId.value),
    encodedValue = Base64.encode("$publicId|${accessLevel.value}".toByteArray()),
  )
}
