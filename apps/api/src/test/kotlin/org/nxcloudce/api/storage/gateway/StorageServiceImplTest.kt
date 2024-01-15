package org.nxcloudce.api.storage.gateway

import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import kotlin.test.Test

@QuarkusTest
class StorageServiceImplTest {
  @InjectMock
  lateinit var fileRepository: FileRepository

  @Inject
  lateinit var storageServiceImpl: StorageServiceImpl

  @Test
  fun `should return GET URL from file repository`() =
    runTest {
      // Given
      coEvery { fileRepository.generateGetUrl("dummy-workspace/dummy-artifact") } returns "get-presigned-url"

      // When
      val result = storageServiceImpl.generateGetUrl(ArtifactId("dummy-artifact"), WorkspaceId("dummy-workspace"))

      // Then
      kotlin.test.assertEquals("get-presigned-url", result)
      coVerify(exactly = 1) { fileRepository.generateGetUrl("dummy-workspace/dummy-artifact") }
    }

  @Test
  fun `should return PUT URL from file repository`() =
    runTest {
      // Given
      coEvery { fileRepository.generatePutUrl("dummy-workspace/dummy-artifact") } returns "put-presigned-url"

      // When
      val result = storageServiceImpl.generatePutUrl(ArtifactId("dummy-artifact"), WorkspaceId("dummy-workspace"))

      // Then
      kotlin.test.assertEquals("put-presigned-url", result)
      coVerify(exactly = 1) { fileRepository.generatePutUrl("dummy-workspace/dummy-artifact") }
    }
}
