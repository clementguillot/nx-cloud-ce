package org.nxcloudce.server.domain.run.interactor

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.gateway.ArtifactRepository
import org.nxcloudce.server.domain.run.gateway.RunRepository
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.gateway.TaskRepository
import org.nxcloudce.server.domain.run.model.*
import org.nxcloudce.server.domain.run.usecase.CleanupRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime
import java.util.*

class CleanupRunImplTest {
  private val mockRunRepository = mockk<RunRepository>()
  private val mockTaskRepository = mockk<TaskRepository>()
  private val mockArtifactRepository = mockk<ArtifactRepository>()
  private val mockStorageService = mockk<StorageService>()
  private val cleanupRunImpl = CleanupRunImpl(mockRunRepository, mockTaskRepository, mockArtifactRepository, mockStorageService)

  @Test
  fun `should purge older runs and their data`() =
    runTest {
      val thresholdDate = LocalDateTime.now().minusDays(31)

      val dummyRuns = listOf(buildRun(thresholdDate))
      val dummyTasks = listOf(buildTask(dummyRuns[0].id, dummyRuns[0].workspaceId), buildTask(dummyRuns[0].id, dummyRuns[0].workspaceId))
      val dummyArtifacts =
        listOf(
          buildArtifact(dummyTasks[0].hash, dummyTasks[0].workspaceId),
          buildArtifact(dummyTasks[1].hash, dummyTasks[1].workspaceId),
        )

      coEvery { mockRunRepository.findAllByCreationDateOlderThan(thresholdDate) } returns dummyRuns
      coEvery { mockTaskRepository.findAllByRunId(any()) } returns dummyTasks
      coEvery { mockArtifactRepository.findByHash(any(), dummyRuns[0].workspaceId) } returns dummyArtifacts

      coEvery { mockStorageService.deleteArtifact(any(), dummyRuns[0].workspaceId) } just runs
      coEvery {
        mockArtifactRepository.delete(
          match {
            dummyArtifacts.contains(it)
          },
        )
      } returns true
      coEvery { mockTaskRepository.deleteAllByRunId(any()) } returns 2L
      coEvery { mockRunRepository.delete(match { dummyRuns.contains(it) }) } returns true

      val result =
        cleanupRunImpl(request = CleanupRunRequest(creationDateThreshold = thresholdDate)) {
          it
        }

      expect(result.deletedCount).toEqual(1)

      coVerify(exactly = 1) { mockStorageService.deleteArtifact(dummyArtifacts[0].id, dummyRuns[0].workspaceId) }
      coVerify(exactly = 1) { mockStorageService.deleteArtifact(dummyArtifacts[1].id, dummyRuns[0].workspaceId) }
      coVerify(exactly = 2) { mockArtifactRepository.delete(match { dummyArtifacts.contains(it) }) }
      coVerify(exactly = 1) { mockTaskRepository.deleteAllByRunId(dummyRuns[0].id) }
      coVerify(exactly = 1) { mockRunRepository.delete(dummyRuns[0]) }
    }

  private fun buildRun(endTime: LocalDateTime = LocalDateTime.now()): Run =
    Run {
      id(UUID.randomUUID().toString())
      workspaceId(UUID.randomUUID().toString())
      command = "test command"
      status = RunStatus.SUCCESS
      startTime = LocalDateTime.now()
      this.endTime = endTime
      runGroup = "group"
      inner = true
      machineInfo = MachineInfo("machineId", "platform", "version", 4)
      meta = emptyMap()
      linkId = "link-id"
    }

  private fun buildTask(
    runId: RunId,
    workspaceId: WorkspaceId,
  ): Task =
    Task {
      taskId = TaskId(UUID.randomUUID().toString())
      this.runId = runId
      this.workspaceId = workspaceId
      hash = Hash(UUID.randomUUID().toString())
      projectName = "project name"
      target = "target"
      startTime = LocalDateTime.now()
      endTime = LocalDateTime.now()
      cacheStatus = CacheStatus.CACHE_MISS
      status = 0
      uploadedToStorage = true
      terminalOutputUploadedToFileStorage = true
      isCacheable = true
      parallelism = true
      params = "params"
      terminalOutput = "terminal output"
      hashDetails = HashDetails(emptyMap(), emptyMap(), emptyMap())
      artifactId = ArtifactId()
      meta = null
    }

  private fun buildArtifact(
    hash: Hash,
    workspaceId: WorkspaceId,
  ): Artifact.Exist =
    Artifact.Exist(
      id = ArtifactId(),
      hash = hash,
      workspaceId = workspaceId,
      put = "put-url",
      get = "get-url",
    )
}
