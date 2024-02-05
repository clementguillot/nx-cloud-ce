package org.nxcloudce.api.domain.run.model

import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime

@JvmInline value class TaskId(val value: String)

class Task private constructor(builder: Builder) {
  companion object {
    operator fun invoke(block: Builder.() -> Unit): Task {
      val builder = Builder()
      block(builder)
      return builder.build()
    }
  }

  val taskId: TaskId
  val runId: RunId
  val workspaceId: WorkspaceId
  val hash: Hash
  val projectName: String
  val target: String
  val startTime: LocalDateTime
  val endTime: LocalDateTime
  val cacheStatus: CacheStatus
  val status: Int
  val uploadedToStorage: Boolean
  val params: String
  val terminalOutput: String

  // val hashDetails
  val artifactId: ArtifactId?

  init {
    requireNotNull(builder.taskId)
    requireNotNull(builder.runId)
    requireNotNull(builder.workspaceId)
    requireNotNull(builder.hash)
    requireNotNull(builder.projectName)
    requireNotNull(builder.target)
    requireNotNull(builder.startTime)
    requireNotNull(builder.endTime)
    requireNotNull(builder.cacheStatus)
    requireNotNull(builder.status)
    requireNotNull(builder.uploadedToStorage)
    requireNotNull(builder.params)
    requireNotNull(builder.terminalOutput)
//    requireNotNull(builder.hashDetails)

    taskId = builder.taskId!!
    runId = builder.runId!!
    workspaceId = builder.workspaceId!!
    hash = builder.hash!!
    projectName = builder.projectName!!
    target = builder.target!!
    startTime = builder.startTime!!
    endTime = builder.endTime!!
    cacheStatus = builder.cacheStatus!!
    status = builder.status!!
    uploadedToStorage = builder.uploadedToStorage!!
    params = builder.params!!
    // hashDetails = builder.hashDetails!!
    terminalOutput = builder.terminalOutput!!
    artifactId = builder.artifactId
  }

  class Builder {
    var taskId: TaskId? = null
    var runId: RunId? = null
    var workspaceId: WorkspaceId? = null
    var hash: Hash? = null
    var projectName: String? = null
    var target: String? = null
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
    var cacheStatus: CacheStatus? = null
    var status: Int? = null
    var uploadedToStorage: Boolean? = null
    var params: String? = null
    var terminalOutput: String? = null

    // val hashDetails
    var artifactId: ArtifactId? = null

    fun build(): Task = Task(this)
  }
}
