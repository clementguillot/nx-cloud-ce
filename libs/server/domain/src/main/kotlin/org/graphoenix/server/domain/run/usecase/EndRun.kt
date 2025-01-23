package org.graphoenix.server.domain.run.usecase

import org.graphoenix.server.domain.UseCase
import org.graphoenix.server.domain.run.model.*
import org.graphoenix.server.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime

interface EndRun : UseCase<EndRunRequest, EndRunResponse>

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
    val meta: Map<String, String>,
    val vcsContext: VcsContext?,
    val linkId: String,
    val projectGraph: ProjectGraph?,
    val hashedContributors: Collection<String>?,
    val sha: String?,
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
    val terminalOutputUploadedToFileStorage: Boolean,
    val isCacheable: Boolean,
    val parallelism: Boolean,
    val params: String,
    val terminalOutput: String,
    val hashDetails: HashDetails,
    val artifactId: ArtifactId?,
    val meta: Map<String, String>?,
  )
}

data class EndRunResponse(
  val run: Run,
)
