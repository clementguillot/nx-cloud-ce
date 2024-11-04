package org.nxcloudce.server.gateway.persistence

import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.gateway.TaskRepository
import org.nxcloudce.server.domain.run.model.RunId
import org.nxcloudce.server.domain.run.model.Task
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.repository.TaskPanacheRepository

@ApplicationScoped
class TaskRepositoryImpl(
  private val taskPanacheRepository: TaskPanacheRepository,
) : TaskRepository {
  override suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task> {
    val entities = tasks.map { it.toEntity(runId, workspaceId) }

    taskPanacheRepository.persist(entities).awaitSuspending()

    return entities.map { it.toDomain() }
  }

  override fun findAllByRunId(runId: RunId): Flow<Task> =
    taskPanacheRepository.findAllByRunId(ObjectId(runId.value)).asFlow().map {
      it.toDomain()
    }

  override suspend fun deleteAllByRunId(runId: RunId): Long =
    taskPanacheRepository.deleteAllByRunId(ObjectId(runId.value)).awaitSuspending()
}
