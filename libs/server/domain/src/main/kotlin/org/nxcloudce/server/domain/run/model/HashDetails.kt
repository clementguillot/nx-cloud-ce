package org.nxcloudce.server.domain.run.model

data class HashDetails(
  val nodes: Map<String, String>,
  val runtime: Map<String, String>?,
  val implicitDeps: Map<String, String>?,
)
