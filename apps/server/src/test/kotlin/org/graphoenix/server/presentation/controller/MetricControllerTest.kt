package org.graphoenix.server.presentation.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import kotlinx.coroutines.test.runTest
import org.graphoenix.server.prepareWorkspaceAndAccessToken
import org.graphoenix.server.presentation.dto.TaskRunnerMetricDto
import org.junit.jupiter.api.Test

@QuarkusTest
class MetricControllerTest {
  @Test
  fun `should return a OK code when saving metrics`() =
    runTest {
      val token = prepareWorkspaceAndAccessToken()

      given()
        .header("authorization", token)
        .header("Content-Type", "application/json")
        .body(
          TaskRunnerMetricDto(
            entries =
              listOf(
                TaskRunnerMetricDto.PerformanceEntry(
                  entryType = "dtePollTasks",
                  success = true,
                  statusCode = 200,
                  durationMs = 20,
                  payloadSize = null,
                ),
              ),
          ),
        ).`when`()
        .post("/save-metrics")
        .then()
        .statusCode(200)
    }
}
