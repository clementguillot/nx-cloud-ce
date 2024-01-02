package org.nxcloudce.api.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.nxcloudce.api.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.presentation.dto.CreateWorkspaceDto
import kotlin.test.Test

@QuarkusTest
class WorkspaceControllerTest {
  @Inject
  lateinit var orgRepository: OrganizationRepository

  @Test
  fun `should return new workspace ID`() =
    runTest {
      val newOrg = orgRepository.create(CreateOrganizationRequest("dummy org"))

      given()
        .header("Content-Type", "application/json")
        .body(
          CreateWorkspaceDto(
            orgId = newOrg.id.value,
            name = "new workspace",
          ),
        )
        .`when`()
        .post("/nx-cloud/private/create-workspace")
        .then()
        .statusCode(200)
        .body("id", `is`(notNullValue()))
    }
}
