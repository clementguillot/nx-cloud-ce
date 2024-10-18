package org.nxcloudce.server.domain.run.model

data class ProjectGraph(
  val nodes: Map<String, Project>?,
  val dependencies: Map<String, List<Dependency>>?,
) {
  data class Project(
    val type: String,
    val name: String,
    val data: Data,
  ) {
    data class Data(
      val root: String,
      val sourceRoot: String?,
      val metadata: Map<String, Any>?,
      val targets: Map<String, Target>,
    ) {
      data class Target(
        val executor: String?,
        val dependsOn: Collection<String>?,
        val options: Map<String, Any>?,
        val configurations: Any?,
        val parallelism: Boolean?,
        val inputs: Collection<Any>?,
        val outputs: Collection<String>?,
        val defaultConfiguration: String?,
        val cache: Boolean?,
      )
    }
  }

  data class Dependency(
    val source: String,
    val target: String,
    val type: String,
  )
}
