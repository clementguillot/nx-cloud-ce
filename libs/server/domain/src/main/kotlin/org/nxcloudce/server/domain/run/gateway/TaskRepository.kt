package org.nxcloudce.server.domain.run.gateway

import kotlinx.coroutines.flow.Flow
import org.nxcloudce.server.domain.run.model.RunId
import org.nxcloudce.server.domain.run.model.Task
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

interface TaskRepository {
  suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task>

  fun findAllByRunId(runId: RunId): Flow<Task>

  suspend fun deleteAllByRunId(runId: RunId): Long
}
