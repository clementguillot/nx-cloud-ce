package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toContainExactly
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.ArtifactEntity

@QuarkusTest
class ArtifactPanacheRepositoryTest {
  @Inject
  lateinit var artifactPanacheRepository: ArtifactPanacheRepository

  @BeforeEach
  fun setUp() {
    runBlocking {
      artifactPanacheRepository.deleteAll().awaitSuspending()
    }
  }

  @Test
  fun `should persist new entity`() =
    runTest {
      val artifactEntity =
        ArtifactEntity(
          id = null,
          artifactId = "someArtifactId",
          hash = "someHash",
          workspaceId = ObjectId(),
        )

      artifactPanacheRepository.persist(artifactEntity).awaitSuspending()
      val count = artifactPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }

  @Test
  fun `should return a list of artifacts matching a list of hashes`() =
    runTest {
      val hashes = listOf("hash1", "hash2", "hash3")
      val workspaceId = ObjectId()
      hashes.forEach {
        artifactPanacheRepository
          .persist(
            ArtifactEntity(
              id = null,
              artifactId = "someArtifactId",
              hash = it,
              workspaceId = workspaceId,
            ),
          ).awaitSuspending()
      }

      artifactPanacheRepository
        .persist(
          ArtifactEntity(
            id = null,
            artifactId = "someArtifactId",
            hash = "hash4",
            workspaceId = ObjectId(),
          ),
        ).awaitSuspending()

      val result = artifactPanacheRepository.findByHash(hashes, workspaceId.toString())
      val count = artifactPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(4)
      expect(result.size).toEqual(3)
      expect(result.map { it.hash }).toContainExactly("hash1", "hash2", "hash3")
    }

  @Test
  fun `should delete entities by their Artifact ID`() =
    runTest {
      artifactPanacheRepository
        .persist(
          ArtifactEntity(
            id = null,
            artifactId = "artifact-id",
            hash = "has",
            workspaceId = ObjectId(),
          ),
          ArtifactEntity(
            id = null,
            artifactId = "artifact-id-2",
            hash = "has",
            workspaceId = ObjectId(),
          ),
        ).awaitSuspending()

      val result = artifactPanacheRepository.deleteByArtifactId("artifact-id").awaitSuspending()
      val totalCount = artifactPanacheRepository.count().awaitSuspending()

      expect(result).toEqual(1)
      expect(totalCount).toEqual(1)
    }
}
