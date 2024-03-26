package org.nxcloudce.server.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.gateway.ArtifactRepository
import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.ArtifactEntity
import org.nxcloudce.server.persistence.repository.ArtifactPanacheRepository

@ApplicationScoped
class ArtifactRepositoryImpl(
  private val artifactPanacheRepository: ArtifactPanacheRepository,
) : ArtifactRepository {
  override suspend fun findByHash(
    hashes: Collection<Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.Exist> =
    artifactPanacheRepository
      .findByHash(hashes.map { it.value }, workspaceId.value)
      .map { it.toDomain() }

  override suspend fun createWithHash(
    hashes: Collection<Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.New> =
    hashes.map { hash ->
      Artifact.New(
        id = ArtifactId(),
        workspaceId = workspaceId,
        hash = hash,
        put = null,
      )
    }

  override suspend fun createRemoteArtifacts(
    artifact: Map<ArtifactId, Hash>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.Exist> {
    val entities =
      artifact.map {
        ArtifactEntity(
          id = null,
          artifactId = it.key.value,
          hash = it.value.value,
          workspaceId = ObjectId(workspaceId.value),
        )
      }

    artifactPanacheRepository.persist(entities).awaitSuspending()

    return entities.map { it.toDomain() }
  }
}
