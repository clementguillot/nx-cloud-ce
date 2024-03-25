package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.OrganizationEntity

@QuarkusTest
class OrganizationPanacheRepositoryTest {
  @Inject
  lateinit var organizationPanacheRepository: OrganizationPanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val organizationEntity =
        OrganizationEntity(
          id = null,
          name = "Test Organization",
        )
      organizationPanacheRepository.persist(organizationEntity).awaitSuspending()

      val count = organizationPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
