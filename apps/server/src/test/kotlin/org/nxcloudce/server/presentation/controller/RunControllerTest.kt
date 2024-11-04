package org.nxcloudce.server.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.run.model.HashDetails
import org.nxcloudce.server.domain.run.model.MachineInfo
import org.nxcloudce.server.persistence.entity.ArtifactEntity
import org.nxcloudce.server.persistence.repository.AccessTokenPanacheRepository
import org.nxcloudce.server.persistence.repository.ArtifactPanacheRepository
import org.nxcloudce.server.persistence.repository.WorkspacePanacheRepository
import org.nxcloudce.server.prepareWorkspaceAndAccessToken
import org.nxcloudce.server.presentation.dto.RunDto
import org.nxcloudce.server.serializeAndCompress
import java.time.LocalDateTime
import java.util.*

@QuarkusTest
class RunControllerTest {
  @Inject
  lateinit var accessTokenPanacheRepository: AccessTokenPanacheRepository

  @Inject
  lateinit var workspacePanacheRepository: WorkspacePanacheRepository

  @Inject
  lateinit var artifactPanacheRepository: ArtifactPanacheRepository

  @Inject
  lateinit var dispatcher: CoroutineDispatcher

  @Inject
  lateinit var objectMapper: ObjectMapper

  @Test
  fun `should start a new run and return a list of URLs to access cached artifact`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()
      prepareExistingArtifact(token)

      given()
        .header("authorization", token)
        .header("Content-Type", "application/json")
        .body(
          RunDto.Start(
            branch = null,
            runGroup = "run-group",
            ciExecutionId = null,
            ciExecutionEnv = null,
            distributedExecutionId = null,
            hashes = listOf("new-hash", "existing-hash"),
            machineInfo =
              MachineInfo(
                machineId = "junit",
                platform = "test",
                version = "1",
                cpuCores = 1,
              ),
            meta = mapOf("nxCloudVersion" to "123"),
            vcsContext = null,
            clientInstanceId = UUID.randomUUID(),
            clientInstanceSource = "CLOUD_RUNNER",
          ),
        ).`when`()
        .post("/v2/runs/start")
        .then()
        .log()
        .body()
        .statusCode(200)
        .body(
          "artifacts.size()",
          `is`(2),
          "artifacts.existing-hash.artifactUrls.get",
          `is`(notNullValue()),
          "artifacts.existing-hash.artifactUrls.put",
          `is`(notNullValue()),
          "artifacts.new-hash.artifactUrls.get",
          `is`(nullValue()),
          "artifacts.new-hash.artifactUrls.put",
          `is`(notNullValue()),
        )
    }

  @Test
  fun `should end a successful run`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/octet-stream")
        .body(
          serializeAndCompress(
            buildEndRunDto(
              listOf(
                buildTaskDto("1"),
                buildTaskDto("2"),
              ),
              "test-link-id",
            ),
            dispatcher,
            objectMapper,
          ),
        ).`when`()
        .post("/runs/end")
        .then()
        .statusCode(200)
        .body(
          "runUrl",
          `is`("http://localtest/runs/test-link-id"),
          "status",
          `is`("success"),
        )
    }

  @Test
  fun `should end a failure run`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/octet-stream")
        .body(
          serializeAndCompress(
            buildEndRunDto(
              listOf(
                buildTaskDto("1"),
                buildTaskDto("2", 1),
              ),
              "test-link-id",
            ),
            dispatcher,
            objectMapper,
          ),
        ).`when`()
        .post("/runs/end")
        .then()
        .statusCode(200)
        .body(
          "runUrl",
          `is`("http://localtest/runs/test-link-id"),
          "status",
          `is`("success"),
        )
    }

  @Test
  fun `should end run and generate its link ID`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/octet-stream")
        .body(
          serializeAndCompress(
            buildEndRunDto(
              listOf(
                buildTaskDto("1"),
                buildTaskDto("2"),
              ),
              null,
            ),
            dispatcher,
            objectMapper,
          ),
        ).`when`()
        .post("/runs/end")
        .then()
        .statusCode(200)
        .body(
          "status",
          `is`("success"),
        )
    }

  @Test
  fun `should return a healthy response when workspace is authenticated`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .`when`()
        .get("/runs/workspace-status")
        .then()
        .statusCode(200)
    }

  private suspend fun prepareExistingArtifact(token: String) {
    val accessToken = accessTokenPanacheRepository.find("encodedValue", token).firstResult().awaitSuspending()
    val workspace = workspacePanacheRepository.findById(accessToken?.workspaceId!!).awaitSuspending()
    val existingArtifact =
      ArtifactEntity(
        id = null,
        artifactId = UUID.randomUUID().toString(),
        hash = "existing-hash",
        workspaceId = workspace?.id!!,
      )
    artifactPanacheRepository.persist(existingArtifact).awaitSuspending()
  }

  private fun buildEndRunDto(
    tasks: Collection<RunDto.End.Task>,
    linkId: String? = null,
  ): RunDto.End =
    RunDto.End(
      branch = null,
      runGroup = "run-group",
      ciExecutionId = null,
      ciExecutionEnv = null,
      MachineInfo(
        machineId = "junit",
        platform = "test",
        version = "1",
        cpuCores = 1,
      ),
      meta = mapOf("nxCloudVersion" to "123"),
      vcsContext = null,
      tasks = tasks,
      linkId = linkId,
      projectGraph = null,
      projectGraphSha = null,
      hashedContributors = null,
      run =
        RunDto.End.RunData(
          command = "nx run apps/server:test",
          startTime = LocalDateTime.now(),
          endTime = LocalDateTime.now().plusHours(1),
          branch = null,
          runGroup = null,
          inner = false,
          distributedExecutionId = null,
          sha = null,
        ),
      clientInstanceId = UUID.randomUUID(),
      clientInstanceSource = "CLOUD_RUNNER",
    )

  private fun buildTaskDto(
    suffix: String,
    status: Int = 0,
  ): RunDto.End.Task =
    RunDto.End.Task(
      taskId = "task-$suffix",
      hash = "hash-$suffix",
      projectName = "project-$suffix",
      target = "target-$suffix",
      startTime = LocalDateTime.now(),
      endTime = LocalDateTime.now(),
      cacheStatus = "cache-miss",
      status = status,
      uploadedToStorage = true,
      isCacheable = true,
      parallelism = true,
      terminalOutputUploadedToFileStorage = false,
      params = "params-$suffix",
      terminalOutput = "terminal output",
      hashDetails =
        HashDetails(
          nodes = emptyMap(),
          runtime = emptyMap(),
          implicitDeps = emptyMap(),
        ),
      artifactId = UUID.randomUUID(),
      meta = null,
    )
}
