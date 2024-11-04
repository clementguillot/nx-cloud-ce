package org.nxcloudce.server.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.nxcloudce.server.prepareWorkspaceAndAccessToken
import org.nxcloudce.server.presentation.dto.HeartbeatDto
import org.nxcloudce.server.serializeAndCompress

@QuarkusTest
class HeartbeatControllerTest {
  @Inject
  lateinit var dispatcher: CoroutineDispatcher

  @Inject
  lateinit var objectMapper: ObjectMapper

  @Test
  fun `should return a OK code on heartbeat`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/json")
        .body(
          HeartbeatDto(
            ciExecutionId = "test",
            runGroup = "junit",
            logs = null,
          ),
        ).`when`()
        .post("/heartbeat")
        .then()
        .statusCode(200)
    }

  @Test
  fun `should return a OK code when receiving heartbeat logs`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/octet-stream")
        .body(
          serializeAndCompress(
            HeartbeatDto(
              ciExecutionId = "test",
              runGroup = "junit",
              logs = "test logs content",
            ),
            dispatcher,
            objectMapper,
          ),
        ).`when`()
        .post("/heartbeat/logs")
        .then()
        .statusCode(200)
    }
}
