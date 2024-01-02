package org.nxcloudce.api.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import kotlin.test.Test

@QuarkusTest
class PingControllerTest {
  @Test
  fun testHelloEndpoint() {
    given()
      .`when`().get("/ping")
      .then()
      .statusCode(200).body(`is`(""))
  }
}
