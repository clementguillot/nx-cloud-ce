package org.nxcloudce.server.domain.run.gateway

import kotlinx.coroutines.flow.Flow
import org.nxcloudce.server.domain.run.model.Run
import org.nxcloudce.server.domain.run.model.RunStatus
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime

interface RunRepository {
  suspend fun create(
    run: EndRunRequest.Run,
    status: RunStatus,
    workspaceId: WorkspaceId,
  ): Run

  fun findAllByCreationDateOlderThan(date: LocalDateTime): Flow<Run>

  suspend fun delete(run: Run): Boolean
}
