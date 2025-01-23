package org.graphoenix.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.graphoenix.server.persistence.entity.TaskEntity

@ApplicationScoped
class TaskPanacheRepository : ReactivePanacheMongoRepository<TaskEntity> {
  fun findAllByRunId(runId: ObjectId): Multi<TaskEntity> = find(TaskEntity::runId.name, runId).stream()

  fun deleteAllByRunId(runId: ObjectId): Uni<Long> = delete(TaskEntity::runId.name, runId)
}
