package org.nxcloudce.api.domain.workspace.interactor

import GetWorkspaceAccessTokenImpl
import io.mockk.*
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.nxcloudce.api.domain.workspace.gateway.AccessTokenRepository
import org.nxcloudce.api.domain.workspace.model.AccessToken
import org.nxcloudce.api.domain.workspace.usecase.GetWorkspaceAccessTokenRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class GetWorkspaceAccessTokenImplTest {
  @InjectMock
  lateinit var mockAccessTokenRepository: AccessTokenRepository

  @Inject
  lateinit var getWorkspaceAccessToken: GetWorkspaceAccessTokenImpl

  @Test
  fun `should return the access token`() =
    runTest {
      // Given
      val dummyRequest = GetWorkspaceAccessTokenRequest(encodedAccessToken = "test-token")
      val dummyAccessToken = mockk<AccessToken>(relaxed = true)

      coEvery { mockAccessTokenRepository.findByEncodedValue("test-token") } returns dummyAccessToken

      // When
      val result = getWorkspaceAccessToken.getAccessToken(dummyRequest) { it }

      // Then
      assertEquals(dummyAccessToken, result.accessToken)
      coVerify(exactly = 1) { mockAccessTokenRepository.findByEncodedValue("test-token") }
    }
}
