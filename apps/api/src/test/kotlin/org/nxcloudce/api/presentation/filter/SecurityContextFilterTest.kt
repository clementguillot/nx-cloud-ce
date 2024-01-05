package org.nxcloudce.api.presentation.filter

import io.mockk.*
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.SecurityContext
import kotlinx.coroutines.test.runTest
import org.nxcloudce.api.domain.workspace.model.AccessLevel
import org.nxcloudce.api.domain.workspace.model.AccessToken
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessTokenResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@QuarkusTest
class SecurityContextFilterTest {
  @InjectMock
  lateinit var mockGetWorkspaceAccessToken: GetWorkspaceAccessToken

  @Inject
  lateinit var securityContextFilter: SecurityContextFilter

  @Test
  fun `should set security context if request contains a valid API key`() =
    runTest {
      // Given
      val dummyRequestContext = mockk<ContainerRequestContext>(relaxed = true)
      val dummyAccessToken = mockk<AccessToken>()
      val capturePresenter = slot<(GetWorkspaceAccessTokenResponse) -> Unit>()
      var capturedSecurityContext: SecurityContext? = null

      every { dummyRequestContext.headers.getFirst("authorization") } returns "valid API key"
      every { dummyRequestContext.securityContext = any() } answers {
        capturedSecurityContext = firstArg<SecurityContext>()
      }
      every { dummyAccessToken.workspaceId } returns WorkspaceId("workspace-id")
      every { dummyAccessToken.accessLevel } returns AccessLevel.READ_WRITE
      coEvery {
        mockGetWorkspaceAccessToken.getAccessToken(
          GetWorkspaceAccessTokenRequest("valid API key"),
          capture(capturePresenter),
        )
      } answers {
        capturePresenter.captured.invoke(GetWorkspaceAccessTokenResponse(dummyAccessToken))
      }

      // When
      securityContextFilter.preMatchingFilter(dummyRequestContext)

      // Then
      verify(exactly = 1) { dummyRequestContext.securityContext = any() }
      assertNotNull(capturedSecurityContext)
      assertEquals("workspace-id", capturedSecurityContext!!.userPrincipal.name)
      assertEquals("api-key", capturedSecurityContext!!.authenticationScheme)
      assertTrue(capturedSecurityContext!!.isUserInRole(AccessLevel.READ_WRITE.value))
      assertTrue(capturedSecurityContext!!.isSecure)
    }

  @Test
  fun `should do nothing if request contains an invalid API key`() =
    runTest {
      // Given
      val dummyRequestContext = mockk<ContainerRequestContext>(relaxed = true)
      val capturePresenter = slot<(GetWorkspaceAccessTokenResponse) -> Unit>()

      every { dummyRequestContext.headers.getFirst("authorization") } returns "valid API key"
      coEvery {
        mockGetWorkspaceAccessToken.getAccessToken(
          GetWorkspaceAccessTokenRequest("valid API key"),
          capture(capturePresenter),
        )
      } answers {
        capturePresenter.captured.invoke(GetWorkspaceAccessTokenResponse(null))
      }

      // When
      securityContextFilter.preMatchingFilter(dummyRequestContext)

      // Then
      verify(exactly = 0) { dummyRequestContext.securityContext = any() }
    }

  @Test
  fun `should do nothing if request does not contain an API key`() =
    runTest {
      // Given
      val dummyRequestContext = mockk<ContainerRequestContext>(relaxed = true)

      every { dummyRequestContext.headers.getFirst("authorization") } returns null

      // When
      securityContextFilter.preMatchingFilter(dummyRequestContext)

      // Then
      verify(exactly = 0) { dummyRequestContext.securityContext = any() }
    }
}
