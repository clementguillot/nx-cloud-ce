package org.nxcloudce.server.domain.run.gateway

import org.nxcloudce.server.domain.run.model.RunId
import org.nxcloudce.server.domain.run.model.Task
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

fun interface TaskRepository {
  suspend fun create(
    tasks: Collection<EndRunRequest.Task>,
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Collection<Task>
}
