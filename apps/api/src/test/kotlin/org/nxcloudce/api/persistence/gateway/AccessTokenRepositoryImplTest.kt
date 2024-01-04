package org.nxcloudce.api.persistence.gateway

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.api.domain.workspace.model.AccessLevel
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.AccessTokenEntity
import org.nxcloudce.api.persistence.repository.AccessTokenPanacheRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class AccessTokenRepositoryImplTest {
  @InjectMock
  lateinit var mockAccessTokenPanacheRepository: AccessTokenPanacheRepository

  @Inject
  lateinit var accessTokenRepository: AccessTokenRepository

  @Test
  @RunOnVertxContext
  fun `should create a new access token in the DB`(asserter: UniAsserter) {
    // Given
    val dummyEntityId = ObjectId()
    val dummyWorkspaceId = WorkspaceId(ObjectId().toString())

    every { mockAccessTokenPanacheRepository.persist(any<AccessTokenEntity>()) } answers
      {
        (firstArg<AccessTokenEntity>()).id = dummyEntityId
        Uni.createFrom().item(firstArg<AccessTokenEntity>())
      }

    // When
    asserter.assertThat(
      { accessTokenRepository.createDefaultAccessToken(dummyWorkspaceId) },
      { accessToken ->
        assertEquals(dummyEntityId.toString(), accessToken.id.value)
        assertEquals(AccessLevel.READ_WRITE, accessToken.accessLevel)
        assertEquals("default", accessToken.name)
      },
    )
  }
}
