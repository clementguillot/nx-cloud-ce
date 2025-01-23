package org.graphoenix.server.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.graphoenix.server.presentation.dto.CreateOrganizationDto
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test

@QuarkusTest
class OrganizationControllerTest {
  @Test
  fun `should return new object ID`() {
    given()
      .header("Content-Type", "application/json")
      .body(CreateOrganizationDto("my new org"))
      .`when`()
      .post("/private/create-org")
      .then()
      .statusCode(200)
      .body("id", `is`(notNullValue()))
  }
}
