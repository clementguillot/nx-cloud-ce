package org.nxcloudce.api.domain.run.model

import org.nxcloudce.api.domain.workspace.model.WorkspaceId
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

//  val meta: Any
  val vcsContext: String?
  val tasks: Collection<Task>
  val linkId: String

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
//    requireNotNull(builder.meta)
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
//    meta = builder.meta!!
    vcsContext = builder.vcsContext
    tasks = builder.tasks!!
    linkId = builder.linkId!!
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

//    var meta: Any? = null
    var vcsContext: String? = null
    var tasks: Collection<Task>? = null
    var linkId: String? = null

    fun build(): Run = Run(this)
  }
}
