package org.nxcloudce.api.domain.run.gateway

import org.nxcloudce.api.domain.run.model.RunId
import org.nxcloudce.api.domain.run.model.Task
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

fun interface TaskRepository {
  suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task>
}
