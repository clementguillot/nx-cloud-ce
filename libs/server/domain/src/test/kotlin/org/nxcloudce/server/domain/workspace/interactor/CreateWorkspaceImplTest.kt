package org.nxcloudce.server.domain.workspace.interactor

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.exception.OrganizationNotFoundException
import org.nxcloudce.server.domain.workspace.gateway.OrganizationValidationService
import org.nxcloudce.server.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.server.domain.workspace.model.Workspace
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceResponse

class CreateWorkspaceImplTest {
  private var mockWorkspaceRepository = mockk<WorkspaceRepository>()
  private var mockOrgValidationService = mockk<OrganizationValidationService>()
  private var createWorkspace = CreateWorkspaceImpl(mockWorkspaceRepository, mockOrgValidationService)

  @Test
  fun `should throw if request Org ID is not found`() =
    runTest {
      // Given
      val dummyOrgId = OrganizationId("not-found-id")
      val dummyRequest = CreateWorkspaceRequest(orgId = dummyOrgId, name = "fail workspace")

      coEvery { mockOrgValidationService.isValidOrgId(dummyOrgId) } returns false

      // When and then
      expect {
        runBlocking { createWorkspace.create(dummyRequest) {} }
      }.toThrow<OrganizationNotFoundException>()
    }

  @Test
  fun `should return the newly created workspace`() =
    runTest {
      // Given
      val dummyOrgId = OrganizationId("valid-org-id")
      val dummyWorkspace =
        Workspace(
          id = WorkspaceId("123"),
          orgId = dummyOrgId,
          name = "new workspace",
          installationSource = null,
          nxInitDate = null,
        )
      val dummyRequest = CreateWorkspaceRequest(orgId = dummyOrgId, name = "new workspace")
      val dummyResponse = CreateWorkspaceResponse(dummyWorkspace)

      coEvery { mockOrgValidationService.isValidOrgId(dummyOrgId) } returns true
      coEvery { mockWorkspaceRepository.create(dummyRequest) } returns dummyWorkspace

      // When
      val result = createWorkspace.create(dummyRequest) { it }

      // Then
      expect(result).toEqual(dummyResponse)
      coVerify(exactly = 1) { mockWorkspaceRepository.create(dummyRequest) }
    }
}
