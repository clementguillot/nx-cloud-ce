package org.nxcloudce.server.domain.metric.usecase

import org.nxcloudce.server.domain.UseCase
import org.nxcloudce.server.domain.metric.model.TaskRunnerMetric
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

interface SaveMetrics : UseCase<SaveMetricsRequest, SaveMetricsResponse>

data class SaveMetricsRequest(val metrics: Collection<Metric>) {
  data class Metric(
    val workspaceId: WorkspaceId,
    val durationMs: Int,
    val success: Boolean,
    val statusCode: Int,
    val entryType: String,
    val payloadSize: Long?,
  )
}

data class SaveMetricsResponse(val metrics: Collection<TaskRunnerMetric>)
