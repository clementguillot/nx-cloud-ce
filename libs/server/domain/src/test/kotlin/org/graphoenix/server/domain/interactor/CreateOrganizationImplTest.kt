package org.graphoenix.server.domain.interactor

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.graphoenix.server.domain.organization.gateway.OrganizationRepository
import org.graphoenix.server.domain.organization.interactor.CreateOrganizationImpl
import org.graphoenix.server.domain.organization.model.Organization
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationResponse
import org.junit.jupiter.api.Test

class CreateOrganizationImplTest {
  private val mockOrgRepository = mockk<OrganizationRepository>()
  private val createOrganization = CreateOrganizationImpl(mockOrgRepository)

  @Test
  fun `should return the newly created organization`() =
    runTest {
      // Given
      val dummyOrg = Organization(OrganizationId("123"), "new org")
      val dummyRequest = CreateOrganizationRequest(name = "new org")
      val dummyResponse = CreateOrganizationResponse(dummyOrg)

      coEvery { mockOrgRepository.create(dummyRequest) } returns dummyOrg

      // When
      val result = createOrganization(dummyRequest) { it }

      // Then
      expect(result).toEqual(dummyResponse)
      coVerify(exactly = 1) { mockOrgRepository.create(dummyRequest) }
    }
}
