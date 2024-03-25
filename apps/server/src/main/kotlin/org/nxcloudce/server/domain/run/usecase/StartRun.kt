package org.nxcloudce.server.domain.run.usecase

import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

interface StartRun {
  suspend fun <T> start(
    request: StartRunRequest,
    presenter: (StartRunResponse) -> T,
  ): T
}

data class StartRunRequest(val hashes: Collection<Hash>, val workspaceId: WorkspaceId, val canPut: Boolean)

data class StartRunResponse(val artifacts: Collection<Artifact>)
