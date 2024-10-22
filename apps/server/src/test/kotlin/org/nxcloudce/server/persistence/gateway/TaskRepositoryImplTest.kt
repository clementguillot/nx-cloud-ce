package org.nxcloudce.server.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.model.*
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.TaskEntity
import org.nxcloudce.server.persistence.repository.TaskPanacheRepository
import java.time.LocalDateTime

@QuarkusTest
class TaskRepositoryImplTest {
  @InjectMock
  lateinit var mockTaskPanacheRepository: TaskPanacheRepository

  @Inject
  lateinit var taskRepository: TaskRepositoryImpl

  @Test
  fun `should create new tasks in the DB`() =
    runTest {
      // Given
      val tasks =
        listOf(
          EndRunRequest.Task(
            taskId = "task1",
            hash = Hash("hash1"),
            projectName = "project1",
            target = "target1",
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now(),
            cacheStatus = CacheStatus.CACHE_MISS,
            status = 0,
            uploadedToStorage = true,
            terminalOutputUploadedToFileStorage = true,
            isCacheable = true,
            parallelism = true,
            params = "params1",
            terminalOutput = "output1",
            hashDetails =
              HashDetails(
                nodes = mapOf("apps/server:ProjectConfiguration" to "dummy"),
                runtime = emptyMap(),
                implicitDeps = emptyMap(),
              ),
            artifactId = ArtifactId("artifact1"),
            meta = null,
          ),
          EndRunRequest.Task(
            taskId = "task2",
            hash = Hash("hash2"),
            projectName = "project2",
            target = "target2",
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now(),
            cacheStatus = CacheStatus.LOCAL_CACHE_HIT,
            status = 1,
            uploadedToStorage = true,
            terminalOutputUploadedToFileStorage = true,
            isCacheable = true,
            parallelism = true,
            params = "params2",
            terminalOutput = "output2",
            hashDetails =
              HashDetails(
                nodes = mapOf("apps/server:ProjectConfiguration" to "dummy"),
                runtime = emptyMap(),
                implicitDeps = emptyMap(),
              ),
            artifactId = null,
            meta = null,
          ),
        )
      val runId = RunId(ObjectId().toString())
      val workspaceId = WorkspaceId(ObjectId().toString())

      every { mockTaskPanacheRepository.persist(any<Collection<TaskEntity>>()) } answers {
        (firstArg<Collection<TaskEntity>>()).forEach { entity ->
          entity.id = ObjectId()
        }
        Uni.createFrom().voidItem()
      }

      // When
      val result = taskRepository.create(tasks, runId, workspaceId)

      // Then
      expect(result.size).toEqual(tasks.size)
    }

  @Test
  fun `should find tasks by their run ID`() =
    runTest {
      // Given
      val dummyRunId = ObjectId()
      val dummyTaskEntities = listOf(buildTaskEntity(dummyRunId))
      every { mockTaskPanacheRepository.findAllByRunId(dummyRunId) } returns Multi.createFrom().items(*dummyTaskEntities.toTypedArray())

      // When
      val result = taskRepository.findAllByRunId(RunId(dummyRunId.toString())).toList()

      // Then
      expect(result.size).toEqual(1)
      expect(result.toList()[0].taskId.value).toEqual(dummyTaskEntities[0].taskId)
    }

  @Test
  fun `should delete tasks by their run ID`() =
    runTest {
      // Given
      val dummyRunId = ObjectId()
      every { mockTaskPanacheRepository.deleteAllByRunId(dummyRunId) } returns Uni.createFrom().item(1)

      // When
      val result = taskRepository.deleteAllByRunId(RunId(dummyRunId.toString()))

      // Then
      expect(result).toEqual(1)
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
      terminalOutputUploadedToFileStorage = true,
      isCacheable = true,
      parallelism = true,
      params = "params",
      terminalOutput = "output",
      hashDetails =
        TaskEntity.HashDetails(
          nodes = mapOf("node1" to "hash1", "node2" to "hash2"),
          runtime = mapOf("runtime1" to "hash1", "runtime2" to "hash2"),
          implicitDeps = mapOf("dep1" to "hash1", "dep2" to "hash2"),
        ),
      artifactId = null,
      meta = null,
    )
}
