package org.nxcloudce.api.domain.run.usecase

import org.nxcloudce.api.domain.run.model.*
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime

interface EndRun {
  suspend fun <T> end(
    request: EndRunRequest,
    presenter: (EndRunResponse) -> T,
  ): T
}

data class EndRunRequest(
  val run: Run,
  val tasks: Collection<Task>,
  val workspaceId: WorkspaceId,
) {
  data class Run(
    val command: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val branch: String?,
    val runGroup: String,
    val inner: Boolean,
    val distributedExecutionId: String?,
    val ciExecutionId: String?,
    val ciExecutionEnv: String?,
    val machineInfo: MachineInfo,
//    val meta: Any,
    val vcsContext: String?,
    val linkId: String,
  )

  data class Task(
    val taskId: String,
    val hash: Hash,
    val projectName: String,
    val target: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val cacheStatus: CacheStatus,
    val status: Int,
    val uploadedToStorage: Boolean,
    val params: String,
    val terminalOutput: String,
//    val hashDetails
    val artifactId: ArtifactId?,
  )
}

data class EndRunResponse(val run: Run)
