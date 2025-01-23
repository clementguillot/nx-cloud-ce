package org.graphoenix.server.domain.run.usecase

import org.graphoenix.server.domain.UseCase
import org.graphoenix.server.domain.run.model.Artifact
import org.graphoenix.server.domain.run.model.Hash
import org.graphoenix.server.domain.workspace.model.WorkspaceId

interface StartRun : UseCase<StartRunRequest, StartRunResponse>

data class StartRunRequest(
  val hashes: Collection<Hash>,
  val workspaceId: WorkspaceId,
  val canPut: Boolean,
)

data class StartRunResponse(
  val artifacts: Collection<Artifact>,
)
