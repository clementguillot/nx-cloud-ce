package org.nxcloudce.server.storage.gateway

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.runs
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.storage.core.FileRepository

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
      expect(result).toEqual("get-presigned-url")
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
      expect(result).toEqual("put-presigned-url")
      coVerify(exactly = 1) { fileRepository.generatePutUrl("dummy-workspace/dummy-artifact") }
    }

  @Test
  fun `should delete an object from file repository`() =
    runTest {
      // Given
      coEvery { fileRepository.deleteFile("dummy-workspace/dummy-artifact") } just runs

      // When
      storageServiceImpl.deleteArtifact(ArtifactId("dummy-artifact"), WorkspaceId("dummy-workspace"))

      // Then
      coVerify(exactly = 1) { fileRepository.deleteFile("dummy-workspace/dummy-artifact") }
    }
}
