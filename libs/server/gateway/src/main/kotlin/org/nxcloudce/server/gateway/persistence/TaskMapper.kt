package org.nxcloudce.server.gateway.persistence

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
    terminalOutputUploadedToFileStorage = this@toDomain.terminalOutputUploadedToFileStorage
    isCacheable = this@toDomain.isCacheable
    parallelism = this@toDomain.parallelism
    params = this@toDomain.params
    hashDetails =
      HashDetails(
        nodes = this@toDomain.hashDetails.nodes,
        runtime = this@toDomain.hashDetails.runtime,
        implicitDeps = this@toDomain.hashDetails.implicitDeps,
      )
    terminalOutput = this@toDomain.terminalOutput
    artifactId = this@toDomain.artifactId?.let { ArtifactId(it) }
    meta = this@toDomain.meta
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
    terminalOutputUploadedToFileStorage = terminalOutputUploadedToFileStorage,
    isCacheable = isCacheable,
    parallelism = parallelism,
    params = params,
    hashDetails =
      TaskEntity.HashDetails(
        nodes = hashDetails.nodes,
        runtime = hashDetails.runtime,
        implicitDeps = hashDetails.implicitDeps,
      ),
    terminalOutput = terminalOutput,
    artifactId = artifactId?.value,
    meta = meta,
  )
