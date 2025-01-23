package org.graphoenix.server.domain.metric.interactor

import org.graphoenix.server.domain.metric.gateway.TaskRunnerMetricRepository
import org.graphoenix.server.domain.metric.usecase.SaveMetrics
import org.graphoenix.server.domain.metric.usecase.SaveMetricsRequest
import org.graphoenix.server.domain.metric.usecase.SaveMetricsResponse

class SaveMetricsImpl(
  private val taskRunnerMetricRepository: TaskRunnerMetricRepository,
) : SaveMetrics {
  override suspend fun <T> invoke(
    request: SaveMetricsRequest,
    presenter: (SaveMetricsResponse) -> T,
  ): T =
    taskRunnerMetricRepository.save(request.metrics).let {
      presenter(
        SaveMetricsResponse(
          metrics = it,
        ),
      )
    }
}
