package org.nxcloudce.server.domain.metric.gateway

import org.nxcloudce.server.domain.metric.model.TaskRunnerMetric
import org.nxcloudce.server.domain.metric.usecase.SaveMetricsRequest

fun interface TaskRunnerMetricRepository {
  suspend fun save(metrics: Collection<SaveMetricsRequest.Metric>): Collection<TaskRunnerMetric>
}
