package org.graphoenix.server.domain.run.gateway

import kotlinx.coroutines.flow.Flow
import org.graphoenix.server.domain.run.model.Run
import org.graphoenix.server.domain.run.model.RunStatus
import org.graphoenix.server.domain.run.usecase.EndRunRequest
import org.graphoenix.server.domain.workspace.model.WorkspaceId
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
