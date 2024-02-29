package org.nxcloudce.api.domain.workspace.interactor

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.api.domain.workspace.gateway.OrganizationCreationService
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.model.*
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class CreateOrgAndWorkspaceImplTest {
  @InjectMock
  lateinit var mockWorkspaceRepository: WorkspaceRepository

  @InjectMock
  lateinit var mockOrgCreation: OrganizationCreationService

  @InjectMock
  lateinit var mockAccessTokenRepository: AccessTokenRepository

  @Inject
  lateinit var createOrgAndWorkspace: CreateOrgAndWorkspaceImpl

  @Test
  @RunOnVertxContext
  fun `should create org, workspace and default access token`(uniAsserter: UniAsserter) {
    // Given
    val dummyOrgId = OrganizationId("org-id")
    val dummyWorkspaceId = WorkspaceId("workspace-id")
    val dummyRequest =
      CreateOrgAndWorkspaceRequest(workspaceName = "test workspace", installationSource = "junit", nxInitDate = LocalDateTime.now())

    every { mockOrgCreation.createOrg("test workspace") } returns
      Uni.createFrom().item(Organization(dummyOrgId, "test workspace"))
    every { mockWorkspaceRepository.create(dummyRequest, dummyOrgId) } returns
      Uni.createFrom()
        .item(
          Workspace(dummyWorkspaceId, dummyOrgId, "test workspace", "junit", null),
        )
    every { mockAccessTokenRepository.createDefaultAccessToken(dummyWorkspaceId) } returns
      Uni.createFrom()
        .item(
          AccessToken {
            id = AccessTokenId("token-ID")
            workspaceId = dummyWorkspaceId
            accessLevel = AccessLevel.READ_WRITE
            name = "default"
            publicId = AccessTokenPublicId()
            encodedValue = "base64content"
          },
        )

    // When
    uniAsserter.assertThat(
      { createOrgAndWorkspace.create(dummyRequest) { it } },
      { response ->
        // Then
        assertEquals("test workspace", response.workspace.name)
        assertEquals(dummyWorkspaceId, response.workspace.id)
        assertEquals("default", response.accessToken.name)
        assertEquals(AccessLevel.READ_WRITE, response.accessToken.accessLevel)
      },
    )
  }
}
