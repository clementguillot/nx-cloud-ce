package org.nxcloudce.server.domain.run.interactor

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toBeAnInstanceOf
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.gateway.ArtifactRepository
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.run.usecase.StartRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

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
      val firstArtifact = result.artifacts.first()
      expect(firstArtifact).toBeAnInstanceOf<Artifact.Exist>()
      val resultArtifact = firstArtifact as Artifact.Exist
      expect(resultArtifact) {
        its { get }.toEqual("test-get-url")
        its { put }.toEqual("test-put-url")
      }
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
      val firstArtifact = result.artifacts.first()
      expect(firstArtifact).toBeAnInstanceOf<Artifact.New>()
      val resultArtifact = result.artifacts.first() as Artifact.New
      expect(resultArtifact.put).toEqual("test-put-url")
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
      val firstArtifact = result.artifacts.first()
      expect(firstArtifact).toBeAnInstanceOf<Artifact.Exist>()
      val resultArtifact = result.artifacts.first() as Artifact.Exist
      expect(resultArtifact) {
        its { get }.toEqual("test-get-url")
        its { put }.toEqual(null)
      }
      coVerify(exactly = 1) { mockArtifactRepository.createWithHash(emptyList(), dummyWorkspaceId) }
    }
}
