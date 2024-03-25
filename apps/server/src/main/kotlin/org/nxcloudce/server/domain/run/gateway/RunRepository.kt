package org.nxcloudce.server.domain.run.gateway

import org.nxcloudce.server.domain.run.model.Run
import org.nxcloudce.server.domain.run.model.RunStatus
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

fun interface RunRepository {
  suspend fun create(
    run: EndRunRequest.Run,
    status: RunStatus,
    workspaceId: WorkspaceId,
  ): Run
}
