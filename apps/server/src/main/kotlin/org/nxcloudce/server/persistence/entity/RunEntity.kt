package org.nxcloudce.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.model.ProjectGraph
import org.nxcloudce.server.domain.run.model.VcsContext
import java.time.LocalDateTime

@MongoEntity(collection = "run")
data class RunEntity(
  var id: ObjectId?,
  var workspaceId: ObjectId,
  var command: String,
  var status: String,
  var startTime: LocalDateTime,
  var endTime: LocalDateTime,
  var branch: String?,
  var runGroup: String,
  var inner: Boolean,
  var distributedExecutionId: String?,
  var ciExecutionId: String?,
  var ciExecutionEnv: String?,
  var machineInfo: MachineInfo,
  var meta: Map<String, String>,
  var vcsContext: VcsContext?,
  var linkId: String,
  var projectGraph: ProjectGraph?,
  var hashedContributors: String?,
  var sha: String?,
) {
  data class MachineInfo(
    var machineId: String = "",
    var platform: String = "",
    var version: String = "",
    var cpuCores: Short = 0,
  )
}
