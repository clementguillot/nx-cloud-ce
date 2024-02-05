package org.nxcloudce.api.domain.run.model

import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskTest {
  @Test
  fun `should build a new instance of Task`() {
    val dummyStartTime = LocalDateTime.now()
    val dummyEndTime = dummyStartTime.plusHours(1)
    val task =
      Task {
        taskId = TaskId("task-id")
        runId = RunId("run-id")
        workspaceId = WorkspaceId("workspace-id")
        hash = Hash("hash-value")
        projectName = "apps/api"
        target = "test"
        startTime = dummyStartTime
        endTime = dummyEndTime
        cacheStatus = CacheStatus.CACHE_MISS
        status = 0
        uploadedToStorage = true
        params = "params"
        terminalOutput = "terminal-output"
        artifactId = ArtifactId("artifact-id")
      }

    assertEquals("task-id", task.taskId.value)
    assertEquals("run-id", task.runId.value)
    assertEquals("workspace-id", task.workspaceId.value)
    assertEquals("hash-value", task.hash.value)
    assertEquals("apps/api", task.projectName)
    assertEquals("test", task.target)
    assertEquals(dummyStartTime, task.startTime)
    assertEquals(dummyEndTime, task.endTime)
    assertEquals(CacheStatus.CACHE_MISS, task.cacheStatus)
    assertEquals(0, task.status)
    assertTrue(task.uploadedToStorage)
    assertEquals("params", task.params)
    assertEquals("terminal-output", task.terminalOutput)
    assertEquals("artifact-id", task.artifactId?.value)
  }
}
