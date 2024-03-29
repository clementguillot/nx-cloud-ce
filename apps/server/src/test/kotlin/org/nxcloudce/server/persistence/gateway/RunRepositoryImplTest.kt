package org.nxcloudce.server.persistence.gateway

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.model.*
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.entity.RunEntity
import org.nxcloudce.server.persistence.repository.RunPanacheRepository
import java.time.LocalDateTime

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
                    ProjectGraph.Node(
                      type = "application",
                      name = "apps/server",
                      data = mapOf("root" to "apps/server/src"),
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
}
