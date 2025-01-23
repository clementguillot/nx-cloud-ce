package org.graphoenix.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.graphoenix.server.persistence.entity.AccessTokenEntity
import org.junit.jupiter.api.Test

@QuarkusTest
class AccessTokenPanacheRepositoryTest {
  @Inject
  lateinit var accessTokenPanacheRepository: AccessTokenPanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val entity =
        AccessTokenEntity(
          id = null,
          name = "Test",
          publicId = "123456",
          accessLevel = "admin",
          workspaceId = ObjectId(),
          encodedValue = "encodedValue",
        )
      accessTokenPanacheRepository.persist(entity).awaitSuspending()

      val count = accessTokenPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
