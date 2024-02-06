package org.nxcloudce.api.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.*
import kotlin.test.Test

@QuarkusTest
class ClientControllerTest {
  @Test
  fun `should return a valid bundle if the version matches`() {
    given()
      .`when`().get("/nx-cloud/client/verify?version=dummy-version")
      .then()
      .statusCode(200)
      .body(
        "valid",
        `is`(equalTo(true)),
        "url",
        `is`(nullValue()),
        "version",
        `is`(nullValue()),
      )
  }

  @Test
  fun `should return an invalid bundle if the version is empty or different`() {
    given()
      .`when`().get("/nx-cloud/client/verify")
      .then()
      .statusCode(200)
      .body(
        "valid",
        `is`(equalTo(false)),
        "url",
        `is`(equalTo("http://localtest/static/client-bundle.gz")),
        "version",
        `is`(equalTo("dummy-version")),
      )
    given()
      .`when`().get("/nx-cloud/client/verify?version=not-matching")
      .then()
      .statusCode(200)
      .body(
        "valid",
        `is`(equalTo(false)),
        "url",
        `is`(equalTo("http://localtest/static/client-bundle.gz")),
        "version",
        `is`(equalTo("dummy-version")),
      )
  }
}