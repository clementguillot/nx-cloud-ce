package org.nxcloudce.server.persistence.gateway

import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.model.*
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.TaskEntity

fun TaskEntity.toDomain(): Task =
  Task {
    taskId = TaskId(this@toDomain.taskId)
    runId = RunId(this@toDomain.runId.toString())
    workspaceId = WorkspaceId(this@toDomain.workspaceId.toString())
    hash = Hash(this@toDomain.hash)
    projectName = this@toDomain.projectName
    target = this@toDomain.target
    startTime = this@toDomain.startTime
    endTime = this@toDomain.endTime
    cacheStatus = CacheStatus.from(this@toDomain.cacheStatus)
    status = this@toDomain.status
    uploadedToStorage = this@toDomain.uploadedToStorage
    params = this@toDomain.params
    hashDetails = this@toDomain.hashDetails
    terminalOutput = this@toDomain.terminalOutput
    artifactId = this@toDomain.artifactId?.let { ArtifactId(it) }
  }

fun EndRunRequest.Task.toEntity(
  runId: RunId,
  workspaceId: WorkspaceId,
): TaskEntity =
  TaskEntity(
    id = null,
    runId = ObjectId(runId.value),
    workspaceId = ObjectId(workspaceId.value),
    taskId = taskId,
    hash = hash.value,
    projectName = projectName,
    target = target,
    startTime = startTime,
    endTime = endTime,
    cacheStatus = cacheStatus.value,
    status = status,
    uploadedToStorage = uploadedToStorage,
    params = params,
    hashDetails = hashDetails,
    terminalOutput = terminalOutput,
    artifactId = artifactId?.value,
  )
