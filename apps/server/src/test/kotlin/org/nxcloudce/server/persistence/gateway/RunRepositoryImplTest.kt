package org.nxcloudce.server.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.mockk.verify
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
import org.nxcloudce.server.persistence.entity.RunEntity
import org.nxcloudce.server.persistence.repository.RunPanacheRepository
import java.time.LocalDateTime
import java.util.*

@QuarkusTest
class RunRepositoryImplTest {
  @InjectMock
  lateinit var mockRunPanacheRepository: RunPanacheRepository

  @Inject
  lateinit var runRepository: RunRepositoryImpl

  @Test
  fun `should create a new run in the DB`() =
    runTest {
      // Given
      val workspaceId = WorkspaceId(ObjectId().toString())
      val runRequest =
        EndRunRequest.Run(
          command = "nx test apps/server",
          startTime = LocalDateTime.now(),
          endTime = LocalDateTime.now(),
          branch = "main",
          runGroup = "",
          inner = false,
          distributedExecutionId = null,
          ciExecutionId = null,
          ciExecutionEnv = null,
          machineInfo =
            MachineInfo(
              machineId = "machine-id",
              platform = "junit",
              version = "42",
              cpuCores = 42,
            ),
          meta = mapOf("nxCloudVersion" to "123"),
          vcsContext =
            VcsContext(
              branch = "main",
              ref = null,
              title = null,
              headSha = null,
              baseSha = null,
              commitLink = null,
              author = "clement guillot",
              authorUrl = null,
              authorAvatarUrl = null,
              repositoryUrl = "https://github.com/clementguillot/nx-cloud-ce",
              platformName = "JUNIT",
            ),
          linkId = "test-link",
          projectGraph =
            ProjectGraph(
              nodes =
                mapOf(
                  "apps/server" to
                    ProjectGraph.Project(
                      type = "application",
                      name = "apps/server",
                      data =
                        ProjectGraph.Project.Data(
                          root = "root",
                          sourceRoot = "root",
                          metadata = emptyMap(),
                          targets = emptyMap(),
                        ),
                    ),
                ),
              dependencies =
                mapOf(
                  "apps/server" to
                    listOf(
                      ProjectGraph.Dependency(source = "apps/server", target = "libs/server/domain", type = "static"),
                    ),
                ),
            ),
          hashedContributors = null,
          sha = null,
        )
      val assignedEntityId = ObjectId()

      every { mockRunPanacheRepository.persist(any<RunEntity>()) } answers
        {
          firstArg<RunEntity>().id = assignedEntityId
          Uni.createFrom().item(firstArg<RunEntity>())
        }

      // When
      val result = runRepository.create(runRequest, RunStatus.SUCCESS, workspaceId)

      // Then
      expect(result) {
        its { id }.toEqual(RunId(assignedEntityId.toString()))
        its { linkId }.toEqual("test-link")
        its { command }.toEqual("nx test apps/server")
      }
    }

  @Test
  fun `should find all runs by their end date from the DB`() =
    runTest {
      // Given
      val dummyRuns = listOf(buildRunEntity(), buildRunEntity())
      val thresholdDate = LocalDateTime.now()
      every {
        mockRunPanacheRepository.findAllByEndTimeLowerThan(
          thresholdDate,
        )
      } returns Multi.createFrom().items(*dummyRuns.toTypedArray())

      // When
      val result = runRepository.findAllByCreationDateOlderThan(thresholdDate).toList()

      // Then
      expect(result.size).toEqual(2)
      verify(exactly = 1) { mockRunPanacheRepository.findAllByEndTimeLowerThan(thresholdDate) }
    }

  @Test
  fun `should delete a run by its ID from the DB`() =
    runTest {
      // Given
      val dummyRunId = ObjectId()
      val dummyRun =
        Run {
          id(dummyRunId.toString())
          workspaceId(UUID.randomUUID().toString())
          command = "test command"
          status = RunStatus.SUCCESS
          startTime = LocalDateTime.now()
          endTime = LocalDateTime.now()
          runGroup = "group"
          inner = true
          machineInfo = MachineInfo("machineId", "platform", "version", 4)
          meta = emptyMap()
          linkId = "link-id"
        }
      every { mockRunPanacheRepository.deleteById(dummyRunId) } returns Uni.createFrom().item(true)

      // When
      val result = runRepository.delete(dummyRun)

      // Then
      expect(result).toEqual(true)
      verify(exactly = 1) { mockRunPanacheRepository.deleteById(dummyRunId) }
    }

  fun buildRunEntity(id: ObjectId = ObjectId()): RunEntity =
    RunEntity(
      id = id,
      workspaceId = ObjectId(),
      command = "test command",
      status = "SUCCESS",
      startTime = LocalDateTime.now(),
      endTime = LocalDateTime.now(),
      branch = "test branch",
      runGroup = "test run group",
      inner = true,
      distributedExecutionId = "test distributed execution id",
      ciExecutionId = "test ci execution id",
      ciExecutionEnv = "test ci execution env",
      machineInfo = RunEntity.MachineInfo(),
      meta = mapOf("key" to "value"),
      vcsContext = null,
      linkId = "test link id",
      projectGraph = null,
      hashedContributors = listOf("test hashed contributors"),
      sha = "test sha",
    )
}
