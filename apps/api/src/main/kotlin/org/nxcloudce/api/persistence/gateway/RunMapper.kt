package org.nxcloudce.api.persistence.gateway

import org.bson.types.ObjectId
import org.nxcloudce.api.domain.run.model.MachineInfo
import org.nxcloudce.api.domain.run.model.Run
import org.nxcloudce.api.domain.run.model.RunId
import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.RunEntity

fun RunEntity.toDomain(): Run =
  Run {
    id = RunId(this@toDomain.id.toString())
    workspaceId = WorkspaceId(this@toDomain.workspaceId.toString())
    command = this@toDomain.command
    status = RunStatus.valueOf(this@toDomain.status)
    startTime = this@toDomain.startTime
    endTime = this@toDomain.endTime
    branch = this@toDomain.branch
    runGroup = this@toDomain.runGroup
    inner = this@toDomain.inner
    distributedExecutionId = this@toDomain.distributedExecutionId
    ciExecutionId = this@toDomain.ciExecutionId
    ciExecutionEnv = this@toDomain.ciExecutionEnv
    machineInfo =
      MachineInfo(
        machineId = this@toDomain.machineInfo.machineId,
        platform = this@toDomain.machineInfo.platform,
        version = this@toDomain.machineInfo.version,
        cpuCores = this@toDomain.machineInfo.cpuCores,
      )
    meta = this@toDomain.meta
    tasks = emptyList()
    vcsContext = this@toDomain.vcsContext
    linkId = this@toDomain.linkId
    projectGraph = this@toDomain.projectGraph
    hashedContributors = this@toDomain.hashedContributors
  }

fun EndRunRequest.Run.toEntity(
  status: RunStatus,
  workspaceId: WorkspaceId,
): RunEntity =
  RunEntity(
    id = null,
    workspaceId = ObjectId(workspaceId.value),
    command = command,
    status = status.name,
    startTime = startTime,
    endTime = endTime,
    branch = branch,
    runGroup = runGroup,
    inner = inner,
    distributedExecutionId = distributedExecutionId,
    ciExecutionId = ciExecutionId,
    ciExecutionEnv = ciExecutionEnv,
    machineInfo =
      RunEntity.MachineInfo().apply {
        machineId = this@toEntity.machineInfo.machineId
        platform = this@toEntity.machineInfo.platform
        version = this@toEntity.machineInfo.version
        cpuCores = this@toEntity.machineInfo.cpuCores
      },
    meta = meta,
    vcsContext = vcsContext,
    linkId = linkId,
    projectGraph = projectGraph,
    hashedContributors = hashedContributors,
  )
