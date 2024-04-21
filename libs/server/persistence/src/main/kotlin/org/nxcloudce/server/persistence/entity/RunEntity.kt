package org.nxcloudce.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
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
  var hashedContributors: Collection<String>?,
  var sha: String?,
) {
  @MongoEntity
  data class MachineInfo(
    var machineId: String = "",
    var platform: String = "",
    var version: String = "",
    var cpuCores: Short = 0,
  )

  @MongoEntity
  data class VcsContext(
    val branch: String,
    val ref: String?,
    val title: String?,
    val headSha: String?,
    val baseSha: String?,
    val commitLink: String?,
    val author: String?,
    val authorUrl: String?,
    val authorAvatarUrl: String?,
    val repositoryUrl: String?,
    val platformName: String?,
  )

  @MongoEntity
  data class ProjectGraph(
    val nodes: Map<String, Node>,
    val dependencies: Map<String, List<Dependency>>,
  ) {
    @MongoEntity
    data class Node(
      val type: String,
      val name: String,
      val data: Map<String, String>,
    )

    @MongoEntity
    data class Dependency(
      val source: String,
      val target: String,
      val type: String,
    )
  }
}
