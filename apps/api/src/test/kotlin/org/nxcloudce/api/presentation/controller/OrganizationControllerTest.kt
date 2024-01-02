package org.nxcloudce.api.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.nxcloudce.api.presentation.dto.CreateOrganizationDto
import kotlin.test.Test

@QuarkusTest
class OrganizationControllerTest {
  @Test
  fun `should return new object ID`() {
    given()
      .header("Content-Type", "application/json")
      .body(CreateOrganizationDto("my new org"))
      .`when`()
      .post("/nx-cloud/private/create-org")
      .then()
      .statusCode(200)
      .body("id", `is`(notNullValue()))
  }
}
