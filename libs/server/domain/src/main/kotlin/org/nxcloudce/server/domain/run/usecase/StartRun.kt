package org.nxcloudce.server.domain.run.usecase

import org.nxcloudce.server.domain.UseCase
import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

interface StartRun : UseCase<StartRunRequest, StartRunResponse>

data class StartRunRequest(
  val hashes: Collection<Hash>,
  val workspaceId: WorkspaceId,
  val canPut: Boolean,
)

data class StartRunResponse(
  val artifacts: Collection<Artifact>,
)
