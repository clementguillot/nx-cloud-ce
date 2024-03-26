package org.nxcloudce.server.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.coEvery
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.ArtifactEntity
import org.nxcloudce.server.persistence.repository.ArtifactPanacheRepository

@QuarkusTest
class ArtifactRepositoryImplTest {
  @InjectMock
  lateinit var mockArtifactPanacheRepository: ArtifactPanacheRepository

  @Inject
  lateinit var artifactRepository: ArtifactRepositoryImpl

  @Test
  fun `should find existing artifacts by their hashes`() =
    runTest {
      // Given
      val hashes = listOf("hash1", "hash2")
      val workspaceId = WorkspaceId(ObjectId().toString())
      val artifactEntity1 =
        ArtifactEntity(ObjectId(), "artifactId1", "hash1", ObjectId(workspaceId.value))
      val artifactEntity2 =
        ArtifactEntity(ObjectId(), "artifactId2", "hash2", ObjectId(workspaceId.value))
      val expectedArtifacts =
        listOf(
          artifactEntity1.toDomain(),
          artifactEntity2.toDomain(),
          Artifact.Exist(ArtifactId("lol"), Hash("lol"), WorkspaceId("caca"), null, null),
        )

      coEvery {
        mockArtifactPanacheRepository.findByHash(
          hashes,
          workspaceId.value,
        )
      } returns listOf(artifactEntity1, artifactEntity2)

      // When
      val result = artifactRepository.findByHash(hashes.map { Hash(it) }, workspaceId)

      // Then
      expect(result).toContainExactly(expectedArtifacts[0], expectedArtifacts[1])
    }

  @Test
  fun `should create new artifacts`() =
    runTest {
      // Given
      val hashes = listOf("hash1", "hash2")
      val workspaceId = WorkspaceId("dummyId")

      // When
      val result = artifactRepository.createWithHash(hashes.map { Hash(it) }, workspaceId)

      // Then
      expect(result.size).toEqual(2)
    }

  @Test
  fun `should create new remote artifacts`() =
    runTest {
      // Given
      val artifact =
        mapOf(
          ArtifactId("artifactId1") to Hash("hash1"),
          ArtifactId("artifactId2") to Hash("hash2"),
        )
      val workspaceId = WorkspaceId(ObjectId().toString())
      val expectedArtifacts =
        listOf(
          Artifact.Exist(
            id = ArtifactId("artifactId1"),
            hash = Hash("hash1"),
            workspaceId = workspaceId,
            put = null,
            get = null,
          ),
          Artifact.Exist(
            id = ArtifactId("artifactId2"),
            hash = Hash("hash2"),
            workspaceId = workspaceId,
            put = null,
            get = null,
          ),
        )
      every {
        mockArtifactPanacheRepository.persist(any<Collection<ArtifactEntity>>())
      } answers {
        (firstArg<Collection<ArtifactEntity>>()).forEach { entity ->
          entity.id = ObjectId()
        }
        Uni.createFrom().voidItem()
      }

      // When
      val result = artifactRepository.createRemoteArtifacts(artifact, workspaceId)

      // Then
      expect(result).toContainExactly(expectedArtifacts[0], expectedArtifacts[1])
    }
}
