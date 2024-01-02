package org.nxcloudce.api.persistence.gateway

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.persistence.entity.OrganizationEntity
import org.nxcloudce.api.persistence.repository.OrganizationPanacheRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
      val dummyEntityWithId = OrganizationEntity(dummyEntityId, "test")
      val dummyRequest = CreateOrganizationRequest("test")

      every { mockOrgPanacheRepository.persist(any<OrganizationEntity>()) } answers {
        // We assign an ID to the new entity
        (firstArg<OrganizationEntity>()).id = dummyEntityId
        Uni.createFrom().item(dummyEntityWithId)
      }

      // When
      val result = orgRepository.create(dummyRequest)

      // Then
      assertEquals(result.id.value, dummyEntityId.toString())
      assertEquals(result.name, dummyEntityWithId.name)
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
      assertTrue(existingId)
      assertFalse(invalidId)
    }
}
