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
    var branch: String,
    var ref: String?,
    var title: String?,
    var headSha: String?,
    var baseSha: String?,
    var commitLink: String?,
    var author: String?,
    var authorUrl: String?,
    var authorAvatarUrl: String?,
    var repositoryUrl: String?,
    var platformName: String?,
  )

  // TODO: we should not use `val`, otherwise, values can't be late-initialized
  // https://github.com/clementguillot/nx-cloud-ce/issues/118
  @MongoEntity
  data class ProjectGraph(
    val nodes: Map<String, Project>,
    val dependencies: Map<String, List<Dependency>>,
  ) {
    @MongoEntity
    data class Project(
      var type: String,
      var name: String,
      var data: Data,
    ) {
      @MongoEntity
      data class Data(
        var root: String,
        var sourceRoot: String?,
        // TODO: can't implement those field due to missing custom codec
        // https://github.com/clementguillot/nx-cloud-ce/issues/118
        // var metadata: Map<String, Any>?,
        // var targets: Map<String, Target>,
      ) {
        // @MongoEntity
        // data class Target(
        //   val executor: String?,
        //   val dependsOn: Collection<String>?,
        //   val options: Map<String, Any>?,
        //   val configurations: Any?,
        //   val parallelism: Boolean?,
        //   val inputs: Collection<Any>?,
        //   val outputs: Collection<String>?,
        //   val defaultConfiguration: String?,
        //   val cache: Boolean?,
        // )
      }
    }

    @MongoEntity
    data class Dependency(
      var source: String,
      var target: String,
      var type: String,
    )
  }
}
