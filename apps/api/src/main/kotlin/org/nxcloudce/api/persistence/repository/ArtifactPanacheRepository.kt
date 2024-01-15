package org.nxcloudce.api.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.ArtifactEntity

@ApplicationScoped
class ArtifactPanacheRepository : ReactivePanacheMongoRepository<ArtifactEntity> {
  suspend fun findByHash(
    hashes: Collection<String>,
    workspaceId: WorkspaceId,
  ): List<ArtifactEntity> =
    find(
      "${ArtifactEntity::hash.name} in ?1 and ${ArtifactEntity::workspaceId.name} = ?2",
      hashes,
      ObjectId(workspaceId.value),
    ).list().awaitSuspending()
}
