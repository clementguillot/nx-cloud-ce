package org.graphoenix.server.gateway.persistence

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.graphoenix.server.domain.metric.usecase.SaveMetricsRequest
import org.graphoenix.server.domain.workspace.model.WorkspaceId
import org.graphoenix.server.persistence.entity.TaskRunnerMetricEntity
import org.graphoenix.server.persistence.repository.TaskRunnerMetricPanacheRepository
import org.junit.jupiter.api.Test

@QuarkusTest
class TaskRunnerMetricRepositoryImplTest {
  @InjectMock
  lateinit var mockTaskRunnerMetricPanacheRepository: TaskRunnerMetricPanacheRepository

  @Inject
  lateinit var taskRunnerMetricRepository: TaskRunnerMetricRepositoryImpl

  @Test
  fun `should create new task runner metrics in the DB`() =
    runTest {
      // Given
      val dummyWorkspaceId = WorkspaceId(ObjectId().toString())
      val dummyMetrics =
        listOf(
          SaveMetricsRequest.Metric(
            workspaceId = dummyWorkspaceId,
            entryType = "createRunGroup",
            success = true,
            statusCode = 200,
            durationMs = 20,
            payloadSize = null,
          ),
          SaveMetricsRequest.Metric(
            workspaceId = dummyWorkspaceId,
            entryType = "completeRunGroup",
            success = true,
            statusCode = 200,
            durationMs = 20,
            payloadSize = null,
          ),
        )

      every { mockTaskRunnerMetricPanacheRepository.persist(any<Collection<TaskRunnerMetricEntity>>()) } answers {
        (firstArg<Collection<TaskRunnerMetricEntity>>()).forEach { entity -> entity.id = ObjectId() }
        Uni.createFrom().voidItem()
      }

      // When
      val result = taskRunnerMetricRepository.save(dummyMetrics)

      // Then
      expect(result.size).toEqual(dummyMetrics.size)
    }
}
