package org.nxcloudce.api.domain.organization.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.nxcloudce.api.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationResponse
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class CreateOrganizationImplTest {
  @InjectMock
  lateinit var mockOrgRepository: OrganizationRepository

  @Inject
  lateinit var createOrganization: CreateOrganizationImpl

  @Test
  fun `should return the newly created organization`() =
    runTest {
      // Given
      val dummyOrg = Organization(OrganizationId("123"), "new org")
      val dummyRequest = CreateOrganizationRequest(name = "new org")
      val dummyResponse = CreateOrganizationResponse(dummyOrg)

      coEvery { mockOrgRepository.create(dummyRequest) } returns dummyOrg

      // When
      val result =
        createOrganization.create(dummyRequest) {
          it
        }

      // Then
      assertEquals(dummyResponse, result)
      coVerify(exactly = 1) { mockOrgRepository.create(dummyRequest) }
    }
}
