package org.graphoenix.server.domain.metric.gateway

import org.graphoenix.server.domain.metric.model.TaskRunnerMetric
import org.graphoenix.server.domain.metric.usecase.SaveMetricsRequest

fun interface TaskRunnerMetricRepository {
  suspend fun save(metrics: Collection<SaveMetricsRequest.Metric>): Collection<TaskRunnerMetric>
}
