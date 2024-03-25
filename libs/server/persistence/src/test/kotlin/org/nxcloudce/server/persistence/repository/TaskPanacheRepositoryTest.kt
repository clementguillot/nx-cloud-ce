package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.TaskEntity
import java.time.LocalDateTime

@QuarkusTest
class TaskPanacheRepositoryTest {
  @Inject
  lateinit var taskPanacheRepository: TaskPanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val taskEntity =
        TaskEntity(
          id = null,
          runId = ObjectId(),
          workspaceId = ObjectId(),
          taskId = "task123",
          hash = "hash123",
          projectName = "project",
          target = "target",
          startTime = LocalDateTime.now(),
          endTime = LocalDateTime.now(),
          cacheStatus = "cached",
          status = 1,
          uploadedToStorage = true,
          params = "params",
          terminalOutput = "output",
          hashDetails =
            TaskEntity.HashDetails(
              nodes = mapOf("node1" to "hash1", "node2" to "hash2"),
              runtime = mapOf("runtime1" to "hash1", "runtime2" to "hash2"),
              implicitDeps = mapOf("dep1" to "hash1", "dep2" to "hash2"),
            ),
          artifactId = null,
        )
      taskPanacheRepository.persist(taskEntity).awaitSuspending()

      val count = taskPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
