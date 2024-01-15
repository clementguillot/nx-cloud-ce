package org.nxcloudce.api.presentation.dto

data class StartRunDto(
  val branch: String?,
  val runGroup: String,
  val ciExecutionId: String?,
  val ciExecutionEnv: String?,
  val distributedExecutionId: String?,
  val hashes: Collection<String>,
  val machineInfo: MachineInfo,
  val meta: Any,
  val vcsContext: String?,
) {
  data class MachineInfo(
    val machineId: String,
    val platform: String,
    val version: String,
    val cpuCores: Int,
  )
}
