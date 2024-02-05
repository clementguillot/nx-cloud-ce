package org.nxcloudce.api.domain.run.gateway

import org.nxcloudce.api.domain.run.model.Run
import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

fun interface RunRepository {
  suspend fun create(
    run: EndRunRequest.Run,
    status: RunStatus,
    workspaceId: WorkspaceId,
  ): Run
}
