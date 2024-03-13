package org.nxcloudce.api.persistence.gateway

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
import org.nxcloudce.api.domain.run.model.MachineInfo
import org.nxcloudce.api.domain.run.model.RunId
import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.entity.RunEntity
import org.nxcloudce.api.persistence.repository.RunPanacheRepository
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
          command = "nx test apps/api",
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
          vcsContext = null,
          linkId = "test-link",
          projectGraph = null,
          hashedContributors = null,
        )
      val assignedEntityId = ObjectId()

      every { mockRunPanacheRepository.persist(any<RunEntity>()) } answers {
        firstArg<RunEntity>().id = assignedEntityId
        Uni.createFrom().item(firstArg<RunEntity>())
      }

      // When
      val result = runRepository.create(runRequest, RunStatus.SUCCESS, workspaceId)

      // Then
      expect(result) {
        its { id }.toEqual(RunId(assignedEntityId.toString()))
        its { linkId }.toEqual("test-link")
        its { command }.toEqual("nx test apps/api")
      }
    }
}
