package org.graphoenix.server.domain.run.gateway

import kotlinx.coroutines.flow.Flow
import org.graphoenix.server.domain.run.model.RunId
import org.graphoenix.server.domain.run.model.Task
import org.graphoenix.server.domain.run.usecase.EndRunRequest
import org.graphoenix.server.domain.workspace.model.WorkspaceId

interface TaskRepository {
  suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task>

  fun findAllByRunId(runId: RunId): Flow<Task>

  suspend fun deleteAllByRunId(runId: RunId): Long
}
