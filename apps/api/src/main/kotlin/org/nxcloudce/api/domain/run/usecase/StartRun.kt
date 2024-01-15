package org.nxcloudce.api.domain.run.usecase

import org.nxcloudce.api.domain.run.model.Artifact
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

interface StartRun {
  suspend fun <T> start(
    request: StartRunRequest,
    presenter: (StartRunResponse) -> T,
  ): T
}

data class StartRunRequest(val hashes: Collection<Hash>, val workspaceId: WorkspaceId, val canPut: Boolean)

data class StartRunResponse(val artifacts: Collection<Artifact>)
