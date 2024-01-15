package org.nxcloudce.api.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.nxcloudce.api.persistence.entity.ArtifactEntity
import org.nxcloudce.api.persistence.repository.ArtifactPanacheRepository
import org.nxcloudce.api.persistence.repository.WorkspacePanacheRepository
import org.nxcloudce.api.presentation.dto.CreateOrgAndWorkspaceDto
import org.nxcloudce.api.presentation.dto.InitWorkspaceDto
import org.nxcloudce.api.presentation.dto.StartRunDto
import java.util.*
import kotlin.test.Test

@QuarkusTest
class RunControllerTest {
  @Inject
  lateinit var workspacePanacheRepository: WorkspacePanacheRepository

  @Inject
  lateinit var artifactPanacheRepository: ArtifactPanacheRepository

  @Test
  fun `should start a new run and return a list of URLs to access cached artifact`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()
      prepareExistingArtifact()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/json")
        .body(
          StartRunDto(
            branch = null,
            runGroup = "run-group",
            ciExecutionId = null,
            ciExecutionEnv = null,
            distributedExecutionId = null,
            hashes = listOf("new-hash", "existing-hash"),
            machineInfo =
              StartRunDto.MachineInfo(
                machineId = "junit",
                platform = "test",
                version = "1",
                cpuCores = 1,
              ),
            meta = "1",
            vcsContext = null,
          ),
        )
        .`when`()
        .post("/nx-cloud/v2/runs/start")
        .then()
        .statusCode(200)
        .body(
          "artifacts.size()", `is`(2),
          "artifacts.existing-hash.artifactUrls.get", `is`(notNullValue()),
          "artifacts.existing-hash.artifactUrls.put", `is`(notNullValue()),
          "artifacts.new-hash.artifactUrls.get", `is`(nullValue()),
          "artifacts.new-hash.artifactUrls.put", `is`(notNullValue()),
        )

      println("coucou $token")
    }

  private suspend fun prepareWorkspaceAndAccessToken(): String {
    val response =
      given()
        .header("Content-Type", "application/json")
        .body(
          CreateOrgAndWorkspaceDto(
            workspaceName = "test-workspace",
            installationSource = "junit",
          ),
        )
        .post("/nx-cloud/create-org-and-workspace")
        .`as`(InitWorkspaceDto::class.java)

    return response.token
  }

  private suspend fun prepareExistingArtifact() {
    val workspace = workspacePanacheRepository.findAll().firstResult().awaitSuspending()
    val existingArtifact =
      ArtifactEntity(
        id = null,
        artifactId = UUID.randomUUID().toString(),
        hash = "existing-hash",
        workspaceId = workspace?.id!!,
      )
    artifactPanacheRepository.persist(existingArtifact).awaitSuspending()
  }
}
