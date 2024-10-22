package org.nxcloudce.server.gateway.persistence

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.domain.metric.gateway.TaskRunnerMetricRepository
import org.nxcloudce.server.domain.metric.model.TaskRunnerMetric
import org.nxcloudce.server.domain.metric.usecase.SaveMetricsRequest
import org.nxcloudce.server.persistence.repository.TaskRunnerMetricPanacheRepository
import java.time.LocalDateTime

@ApplicationScoped
class TaskRunnerMetricRepositoryImpl(
  private val taskRunnerMetricPanacheRepository: TaskRunnerMetricPanacheRepository,
) : TaskRunnerMetricRepository {
  override suspend fun save(metrics: Collection<SaveMetricsRequest.Metric>): Collection<TaskRunnerMetric> {
    val recordingDate = LocalDateTime.now()
    val entities = metrics.map { it.toEntity(recordingDate) }
    taskRunnerMetricPanacheRepository.persist(entities).awaitSuspending()
    return entities.map { it.toDomain() }
  }
}
