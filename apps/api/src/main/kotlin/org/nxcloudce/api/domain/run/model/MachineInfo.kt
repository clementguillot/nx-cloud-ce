package org.nxcloudce.api.domain.run.model

data class MachineInfo(
  val machineId: String,
  val platform: String,
  val version: String,
  val cpuCores: Short,
)
