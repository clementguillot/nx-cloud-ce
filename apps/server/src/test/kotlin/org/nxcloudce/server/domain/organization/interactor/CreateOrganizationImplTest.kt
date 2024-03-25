package org.nxcloudce.server.domain.organization.interactor

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.server.domain.organization.model.Organization
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationResponse

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
      val result = createOrganization.create(dummyRequest) { it }

      // Then
      expect(result).toEqual(dummyResponse)
      coVerify(exactly = 1) { mockOrgRepository.create(dummyRequest) }
    }
}
