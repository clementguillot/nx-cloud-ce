package org.nxcloudce.server.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.server.persistence.entity.OrganizationEntity
import org.nxcloudce.server.persistence.repository.OrganizationPanacheRepository

@QuarkusTest
class OrganizationRepositoryImplTest {
  @InjectMock
  lateinit var mockOrgPanacheRepository: OrganizationPanacheRepository

  @Inject
  lateinit var orgRepository: OrganizationRepositoryImpl

  @Test
  fun `should create a new organization in the DB `() =
    runTest {
      // Given
      val dummyEntityId = ObjectId()
      val dummyRequest = CreateOrganizationRequest("test")

      every { mockOrgPanacheRepository.persist(any<OrganizationEntity>()) } answers
        {
          // We assign an ID to the new entity
          (firstArg<OrganizationEntity>()).id = dummyEntityId
          Uni.createFrom().item(firstArg<OrganizationEntity>())
        }

      // When
      val result = orgRepository.create(dummyRequest)

      // Then
      expect(result) {
        its { id.value }.toEqual(dummyEntityId.toString())
        its { name }.toEqual("test")
      }
    }

  @Test
  fun `should indicate if an org ID is valid or not`() =
    runTest {
      // Given
      val validId = ObjectId()
      val dummyEntity = OrganizationEntity(id = validId, name = "my org")

      every { mockOrgPanacheRepository.findById(validId) } returns Uni.createFrom().item(dummyEntity)
      every { mockOrgPanacheRepository.findById(neq(validId)) } returns Uni.createFrom().nullItem()

      // When
      val existingId = orgRepository.isValidOrgId(OrganizationId(validId.toString()))
      val invalidId = orgRepository.isValidOrgId(OrganizationId(ObjectId().toString()))

      // Then
      expect(existingId).toEqual(true)
      expect(invalidId).toEqual(false)
    }
}
