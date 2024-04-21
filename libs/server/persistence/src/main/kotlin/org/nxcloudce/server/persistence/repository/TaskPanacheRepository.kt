package org.nxcloudce.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.server.persistence.entity.TaskEntity

@ApplicationScoped
class TaskPanacheRepository : ReactivePanacheMongoRepository<TaskEntity> {
  fun findAllByRunId(runId: ObjectId): Uni<List<TaskEntity>> = find(TaskEntity::runId.name, runId).list()

  fun deleteAllByRunId(runId: ObjectId): Uni<Long> = delete(TaskEntity::runId.name, runId)
}
