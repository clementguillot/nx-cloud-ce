package org.nxcloudce.api.persistence.gateway

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.persistence.entity.OrganizationEntity
import org.nxcloudce.api.persistence.repository.OrganizationPanacheRepository
import kotlin.test.assertEquals

@QuarkusTest
class OrganizationGatewayImplTest {
  @InjectMock
  lateinit var mockOrgPanacheRepository: OrganizationPanacheRepository
  private lateinit var orgRepository: OrganizationGatewayImpl

  @BeforeEach
  fun setUp() {
    orgRepository = OrganizationGatewayImpl(mockOrgPanacheRepository)
  }

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
}
