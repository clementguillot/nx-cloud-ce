package org.nxcloudce.server.domain.run.model

data class ProjectGraph(
  val nodes: Map<String, Node>,
  val dependencies: Map<String, List<Dependency>>,
) {
  data class Node(
    val type: String,
    val name: String,
    val data: Map<String, String>,
  )

  data class Dependency(
    val source: String,
    val target: String,
    val type: String,
  )
}
