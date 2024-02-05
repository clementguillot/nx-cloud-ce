package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.run.gateway.TaskRepository
import org.nxcloudce.api.domain.run.model.RunId
import org.nxcloudce.api.domain.run.model.Task
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.repository.TaskPanacheRepository

@ApplicationScoped
class TaskRepositoryImpl(private val taskPanacheRepository: TaskPanacheRepository) : TaskRepository {
  override suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task> {
    val entities = tasks.map { it.toEntity(runId, workspaceId) }

    taskPanacheRepository.persist(entities).awaitSuspending()

    return entities.map { it.toDomain() }
  }
}
