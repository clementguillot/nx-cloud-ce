package org.nxcloudce.api.domain.run.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.api.domain.run.gateway.ArtifactRepository
import org.nxcloudce.api.domain.run.gateway.StorageService
import org.nxcloudce.api.domain.run.model.Artifact
import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.run.usecase.StartRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@QuarkusTest
class StartRunImplTest {
  @InjectMock
  lateinit var mockArtifactRepository: ArtifactRepository

  @InjectMock
  lateinit var mockStorageService: StorageService

  @Inject
  lateinit var startRunImpl: StartRunImpl

  @Test
  fun `should return an existing artifact for a known hash`() =
    runTest {
      // Given
      val dummyWorkspaceId = WorkspaceId("workspace-id")
      val dummyHashes = listOf(Hash("hash"))
      val dummyArtifact =
        Artifact.Exist(
          id = ArtifactId(),
          hash = dummyHashes.first(),
          workspaceId = dummyWorkspaceId,
          get = null,
          put = null,
        )
      coEvery { mockArtifactRepository.findByHash(dummyHashes, dummyWorkspaceId) } returns listOf(dummyArtifact)
      coEvery { mockArtifactRepository.createWithHash(emptyList(), dummyWorkspaceId) } returns emptyList()
      coEvery { mockStorageService.generateGetUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-get-url"
      coEvery { mockStorageService.generatePutUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-put-url"

      val dummyRequest =
        StartRunRequest(
          hashes = dummyHashes,
          workspaceId = dummyWorkspaceId,
          canPut = true,
        )

      // When
      val result = startRunImpl.start(dummyRequest) { it }

      // Then
      assertTrue(result.artifacts.first() is Artifact.Exist)
      val resultArtifact = result.artifacts.first() as Artifact.Exist
      assertEquals("test-get-url", resultArtifact.get)
      assertEquals("test-put-url", resultArtifact.put)
      coVerify(exactly = 1) { mockArtifactRepository.createWithHash(emptyList(), dummyWorkspaceId) }
    }

  @Test
  fun `should return an new artifact for an unknown hash`() =
    runTest {
      // Given
      val dummyWorkspaceId = WorkspaceId("workspace-id")
      val dummyHashes = listOf(Hash("hash"))
      val dummyArtifact =
        Artifact.New(
          id = ArtifactId(),
          hash = dummyHashes.first(),
          workspaceId = dummyWorkspaceId,
          put = null,
        )
      coEvery { mockArtifactRepository.findByHash(dummyHashes, dummyWorkspaceId) } returns emptyList()
      coEvery { mockArtifactRepository.createWithHash(dummyHashes, dummyWorkspaceId) } returns listOf(dummyArtifact)
      coEvery { mockStorageService.generateGetUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-get-url"
      coEvery { mockStorageService.generatePutUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-put-url"

      val dummyRequest =
        StartRunRequest(
          hashes = dummyHashes,
          workspaceId = dummyWorkspaceId,
          canPut = true,
        )

      // When
      val result = startRunImpl.start(dummyRequest) { it }

      // Then
      assertTrue(result.artifacts.first() is Artifact.New)
      val resultArtifact = result.artifacts.first() as Artifact.New
      assertEquals("test-put-url", resultArtifact.put)
      coVerify(exactly = 0) { mockStorageService.generateGetUrl(dummyArtifact.id, dummyWorkspaceId) }
    }

  @Test
  fun `should return a read-only artifact if context is not allowed`() =
    runTest {
      // Given
      val dummyWorkspaceId = WorkspaceId("workspace-id")
      val dummyHashes = listOf(Hash("hash"))
      val dummyArtifact =
        Artifact.Exist(
          id = ArtifactId(),
          hash = dummyHashes.first(),
          workspaceId = dummyWorkspaceId,
          get = null,
          put = null,
        )
      coEvery { mockArtifactRepository.findByHash(dummyHashes, dummyWorkspaceId) } returns listOf(dummyArtifact)
      coEvery { mockArtifactRepository.createWithHash(emptyList(), dummyWorkspaceId) } returns emptyList()
      coEvery { mockStorageService.generateGetUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-get-url"
      coEvery { mockStorageService.generatePutUrl(dummyArtifact.id, dummyWorkspaceId) } returns "test-put-url"

      val dummyRequest =
        StartRunRequest(
          hashes = dummyHashes,
          workspaceId = dummyWorkspaceId,
          canPut = false,
        )

      // When
      val result = startRunImpl.start(dummyRequest) { it }

      // Then
      assertTrue(result.artifacts.first() is Artifact.Exist)
      val resultArtifact = result.artifacts.first() as Artifact.Exist
      assertEquals("test-get-url", resultArtifact.get)
      assertNull(resultArtifact.put)
      coVerify(exactly = 1) { mockArtifactRepository.createWithHash(emptyList(), dummyWorkspaceId) }
    }
}
