package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toContainExactly
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.TaskEntity
import java.time.LocalDateTime

@QuarkusTest
class TaskPanacheRepositoryTest {
  @Inject
  lateinit var taskPanacheRepository: TaskPanacheRepository

  @BeforeEach
  fun setUp() {
    runBlocking {
      taskPanacheRepository.deleteAll().awaitSuspending()
    }
  }

  @Test
  fun `should persist new entity`() =
    runTest {
      val taskEntity = buildTaskEntity()
      taskPanacheRepository.persist(taskEntity).awaitSuspending()

      val count = taskPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }

  @Test
  fun `should find entities by their 'runId'`() =
    runTest {
      val runId = ObjectId()
      taskPanacheRepository.persist(listOf(buildTaskEntity(runId), buildTaskEntity(runId), buildTaskEntity())).awaitSuspending()

      val result = taskPanacheRepository.findAllByRunId(runId).awaitSuspending()
      val totalCount = taskPanacheRepository.count().awaitSuspending()

      expect(totalCount).toEqual(3)
      expect(result.size).toEqual(2)
      expect(result.map { it.runId }.distinct()).toContainExactly(runId)
    }

  @Test
  fun `should delete entities by their 'runId'`() =
    runTest {
      val runId = ObjectId()
      taskPanacheRepository.persist(listOf(buildTaskEntity(runId), buildTaskEntity(runId), buildTaskEntity())).awaitSuspending()

      val result = taskPanacheRepository.deleteAllByRunId(runId).awaitSuspending()
      val totalCount = taskPanacheRepository.count().awaitSuspending()

      expect(totalCount).toEqual(1)
      expect(result).toEqual(2)
    }

  private fun buildTaskEntity(runId: ObjectId = ObjectId()): TaskEntity =
    TaskEntity(
      id = null,
      runId = runId,
      workspaceId = ObjectId(),
      taskId = "task123",
      hash = "hash123",
      projectName = "project",
      target = "target",
      startTime = LocalDateTime.now(),
      endTime = LocalDateTime.now(),
      cacheStatus = "cache-miss",
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
}
