package org.nxcloudce.api.domain.run.model

import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RunTest {
  @Test
  fun `should build a new instance of Run`() {
    val dummyStartTime = LocalDateTime.now()
    val dummyEndTime = dummyStartTime.plusHours(1)
    val run =
      Run {
        id = RunId("run-id")
        workspaceId = WorkspaceId("workspace-id")
        command = "nx test apps/api"
        status = RunStatus.SUCCESS
        startTime = dummyStartTime
        endTime = dummyEndTime
        branch = "main"
        runGroup = "default"
        inner = false
        distributedExecutionId = null
        ciExecutionId = null
        ciExecutionEnv = null
        machineInfo = MachineInfo("machine-id", "linux", "1.0", 4)
        vcsContext = "https://github.com/example/repo.git"
        tasks = emptyList()
        linkId = "link-id"
        projectGraph = "project-graph"
        hashedContributors = null
      }

    assertEquals("run-id", run.id.value)
    assertEquals("workspace-id", run.workspaceId.value)
    assertEquals("nx test apps/api", run.command)
    assertEquals(RunStatus.SUCCESS, run.status)
    assertEquals(dummyStartTime, run.startTime)
    assertEquals(dummyEndTime, run.endTime)
    assertEquals("main", run.branch)
    assertEquals("default", run.runGroup)
    assertEquals(false, run.inner)
    assertEquals(null, run.distributedExecutionId)
    assertEquals(null, run.ciExecutionId)
    assertEquals(null, run.ciExecutionEnv)
    assertEquals(MachineInfo("machine-id", "linux", "1.0", 4), run.machineInfo)
    assertEquals("https://github.com/example/repo.git", run.vcsContext)
    assertEquals(emptyList(), run.tasks)
    assertEquals("link-id", run.linkId)
    assertEquals("project-graph", run.projectGraph)
    assertNull(run.hashedContributors)
  }
}
