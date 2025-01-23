package org.graphoenix.server.presentation.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import org.graphoenix.server.domain.metric.usecase.SaveMetricsRequest
import org.graphoenix.server.domain.workspace.model.WorkspaceId

@RegisterForReflection
data class TaskRunnerMetricDto(
  val entries: Collection<PerformanceEntry>,
) {
  data class PerformanceEntry(
    val durationMs: Int,
    val success: Boolean,
    val statusCode: Int,
    val entryType: String,
    val payloadSize: Long?,
  )

  fun toRequest(workspaceId: WorkspaceId): Collection<SaveMetricsRequest.Metric> =
    entries.map {
      SaveMetricsRequest.Metric(
        workspaceId = workspaceId,
        durationMs = it.durationMs,
        success = it.success,
        statusCode = it.statusCode,
        entryType = it.entryType,
        payloadSize = it.payloadSize,
      )
    }
}
