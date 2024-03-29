package org.nxcloudce.server.domain.run.model

import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime

@JvmInline value class RunId(val value: String)

class Run private constructor(builder: Builder) {
  companion object {
    operator fun invoke(block: Builder.() -> Unit): Run {
      val builder = Builder()
      block(builder)
      return builder.build()
    }
  }

  val id: RunId
  val workspaceId: WorkspaceId
  val command: String
  val status: RunStatus
  val startTime: LocalDateTime
  val endTime: LocalDateTime
  val branch: String?
  val runGroup: String
  val inner: Boolean
  val distributedExecutionId: String?
  val ciExecutionId: String?
  val ciExecutionEnv: String?
  val machineInfo: MachineInfo
  val meta: Map<String, String>
  val vcsContext: VcsContext?
  val tasks: Collection<Task>
  val linkId: String
  val projectGraph: ProjectGraph?
  val hashedContributors: Collection<String>?
  val sha: String?

  init {
    requireNotNull(builder.id)
    requireNotNull(builder.workspaceId)
    requireNotNull(builder.command)
    requireNotNull(builder.status)
    requireNotNull(builder.startTime)
    requireNotNull(builder.endTime)
    requireNotNull(builder.runGroup)
    requireNotNull(builder.runGroup)
    requireNotNull(builder.inner)
    requireNotNull(builder.meta)
    requireNotNull(builder.tasks)
    requireNotNull(builder.linkId)
    id = builder.id!!
    workspaceId = builder.workspaceId!!
    command = builder.command!!
    status = builder.status!!
    startTime = builder.startTime!!
    endTime = builder.endTime!!
    branch = builder.branch
    runGroup = builder.runGroup!!
    inner = builder.inner!!
    distributedExecutionId = builder.distributedExecutionId
    ciExecutionId = builder.ciExecutionId
    ciExecutionEnv = builder.ciExecutionEnv
    machineInfo = builder.machineInfo!!
    meta = builder.meta!!
    vcsContext = builder.vcsContext
    tasks = builder.tasks!!
    linkId = builder.linkId!!
    projectGraph = builder.projectGraph
    hashedContributors = builder.hashedContributors
    sha = builder.sha
  }

  class Builder {
    var id: RunId? = null
    var workspaceId: WorkspaceId? = null
    var command: String? = null
    var status: RunStatus? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
    var branch: String? = null
    var runGroup: String? = null
    var inner: Boolean? = null
    var distributedExecutionId: String? = null
    var ciExecutionId: String? = null
    var ciExecutionEnv: String? = null
    var machineInfo: MachineInfo? = null
    var meta: Map<String, String>? = null
    var vcsContext: VcsContext? = null
    var tasks: Collection<Task>? = null
    var linkId: String? = null
    var projectGraph: ProjectGraph? = null
    var hashedContributors: Collection<String>? = null
    var sha: String? = null

    fun build(): Run = Run(this)
  }
}
