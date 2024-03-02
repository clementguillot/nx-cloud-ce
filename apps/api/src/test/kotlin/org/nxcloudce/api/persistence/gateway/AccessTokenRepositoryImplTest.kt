package org.nxcloudce.api.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.notToEqualNull
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.mockk.mockk
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheQuery
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.api.domain.workspace.model.AccessLevel
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.AccessTokenEntity
import org.nxcloudce.api.persistence.repository.AccessTokenPanacheRepository

@QuarkusTest
class AccessTokenRepositoryImplTest {
  @InjectMock
  lateinit var mockAccessTokenPanacheRepository: AccessTokenPanacheRepository

  @Inject
  lateinit var accessTokenRepository: AccessTokenRepositoryImpl

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
        // Then
        expect(accessToken) {
          its { id.value }.toEqual(dummyEntityId.toString())
          its { accessLevel }.toEqual(AccessLevel.READ_WRITE)
          its { name }.toEqual("default")
        }
      },
    )
  }

  @Test
  fun `should return access token if any`() =
    runTest {
      // Given
      val existingValue = "matching query"
      val dummyEntity =
        AccessTokenEntity(
          id = ObjectId(),
          name = "default",
          publicId = "test",
          accessLevel = "read-write",
          workspaceId = ObjectId(),
          encodedValue = existingValue,
        )
      val mockMatchingQuery = mockk<ReactivePanacheQuery<AccessTokenEntity>>()
      val mockNullQuery = mockk<ReactivePanacheQuery<AccessTokenEntity>>()

      every { mockMatchingQuery.firstResult() } returns Uni.createFrom().item(dummyEntity)
      every { mockNullQuery.firstResult() } returns Uni.createFrom().nullItem()
      every { mockAccessTokenPanacheRepository.find(AccessTokenEntity::encodedValue.name, existingValue) } returns mockMatchingQuery
      every { mockAccessTokenPanacheRepository.find(AccessTokenEntity::encodedValue.name, "not found") } returns mockNullQuery

      // When
      val matchingResult = accessTokenRepository.findByEncodedValue(existingValue)
      val nullResult = accessTokenRepository.findByEncodedValue("not found")

      // Then
      expect(matchingResult).notToEqualNull()
      expect(matchingResult!!.id.value).toEqual(dummyEntity.id.toString())
      expect(nullResult).toEqual(null)
    }
}
