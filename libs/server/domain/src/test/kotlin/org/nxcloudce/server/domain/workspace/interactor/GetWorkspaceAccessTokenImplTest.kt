package org.nxcloudce.server.domain.workspace.interactor

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.server.domain.workspace.model.AccessToken
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessTokenRequest

class GetWorkspaceAccessTokenImplTest {
  private val mockAccessTokenRepository = mockk<AccessTokenRepository>()
  private val getWorkspaceAccessToken = GetWorkspaceAccessTokenImpl(mockAccessTokenRepository)

  @Test
  fun `should return the access token`() =
    runTest {
      // Given
      val dummyRequest = GetWorkspaceAccessTokenRequest(encodedAccessToken = "test-token")
      val dummyAccessToken = mockk<AccessToken>(relaxed = true)

      coEvery { mockAccessTokenRepository.findByEncodedValue("test-token") } returns dummyAccessToken

      // When
      val result = getWorkspaceAccessToken(dummyRequest) { it }

      // Then
      expect(result.accessToken).toEqual(dummyAccessToken)
      coVerify(exactly = 1) { mockAccessTokenRepository.findByEncodedValue("test-token") }
    }
}
