package org.nxcloudce.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.server.persistence.entity.ArtifactEntity

@ApplicationScoped
class ArtifactPanacheRepository : ReactivePanacheMongoRepository<ArtifactEntity> {
  suspend fun findByHash(
    hashes: Collection<String>,
    workspaceId: String,
  ): List<ArtifactEntity> =
    find(
      "${ArtifactEntity::hash.name} in ?1 and ${ArtifactEntity::workspaceId.name} = ?2",
      hashes,
      ObjectId(workspaceId),
    ).list().awaitSuspending()

  fun deleteByArtifactId(artifactId: String): Uni<Long> = delete(ArtifactEntity::artifactId.name, artifactId)
}
