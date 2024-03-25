package org.nxcloudce.server.presentation.infrastructure.filter

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.notToEqualNull
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.*
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.SecurityContext
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.workspace.model.AccessLevel
import org.nxcloudce.server.domain.workspace.model.AccessToken
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenResponse

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
      expect(capturedSecurityContext) {
        notToEqualNull()
        its { this?.userPrincipal!!.name }.toEqual("workspace-id")
        its { this?.authenticationScheme }.toEqual("api-key")
        its { this?.isUserInRole(AccessLevel.READ_WRITE.value) }.toEqual(true)
        its { this?.isSecure }.toEqual(true)
      }
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
