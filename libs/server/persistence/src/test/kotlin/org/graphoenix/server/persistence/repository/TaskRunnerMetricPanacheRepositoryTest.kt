package org.graphoenix.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.graphoenix.server.persistence.entity.TaskRunnerMetricEntity
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@QuarkusTest
class TaskRunnerMetricPanacheRepositoryTest {
  @Inject
  lateinit var taskRunnerMetricPanacheRepository: TaskRunnerMetricPanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val taskRunnerMetricEntity =
        TaskRunnerMetricEntity(
          id = null,
          workspaceId = ObjectId(),
          recordingDate = LocalDateTime.now(),
          durationMs = 300,
          success = true,
          statusCode = 200,
          entryType = "storeFile",
          payloadSize = 42L,
        )
      taskRunnerMetricPanacheRepository.persist(taskRunnerMetricEntity).awaitSuspending()

      val count = taskRunnerMetricPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
