package org.nxcloudce.api.domain.workspace.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.exception.OrganizationNotFoundException
import org.nxcloudce.api.domain.workspace.gateway.OrganizationValidationService
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@QuarkusTest
class CreateWorkspaceImplTest {
  @InjectMock
  lateinit var mockWorkspaceRepository: WorkspaceRepository

  @InjectMock
  lateinit var mockOrgValidationService: OrganizationValidationService

  @Inject
  lateinit var createWorkspace: CreateWorkspaceImpl

  @Test
  fun `should throw if request Org ID is not found`() =
    runTest {
      // Given
      val dummyOrgId = OrganizationId("not-found-id")
      val dummyRequest = CreateWorkspaceRequest(orgId = dummyOrgId, name = "fail workspace")

      coEvery { mockOrgValidationService.isValidOrgId(dummyOrgId) } returns false

      // When and then
      assertFailsWith<OrganizationNotFoundException> {
        createWorkspace.create(dummyRequest) {}
      }
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
        )
      val dummyRequest = CreateWorkspaceRequest(orgId = dummyOrgId, name = "new workspace")
      val dummyResponse = CreateWorkspaceResponse(dummyWorkspace)

      coEvery { mockOrgValidationService.isValidOrgId(dummyOrgId) } returns true
      coEvery { mockWorkspaceRepository.create(dummyRequest) } returns dummyWorkspace

      // When
      val result = createWorkspace.create(dummyRequest) { it }

      // Then
      assertEquals(dummyResponse, result)
      coVerify(exactly = 1) { mockWorkspaceRepository.create(dummyRequest) }
    }
}
