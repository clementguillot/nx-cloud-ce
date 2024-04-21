package org.nxcloudce.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.persistence.entity.RunEntity
import java.time.LocalDateTime

@ApplicationScoped
class RunPanacheRepository : ReactivePanacheMongoRepository<RunEntity> {
  fun findAllByEndTimeLowerThan(creationDate: LocalDateTime): Uni<List<RunEntity>> {
    return find("${RunEntity::endTime.name} < ?1", creationDate).list()
  }
}
