package org.graphoenix.server.presentation.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import org.graphoenix.server.domain.run.usecase.EndRunResponse

@RegisterForReflection
data class RunSummaryDto(
  val runUrl: String,
  val status: String,
) {
  companion object {
    fun from(
      response: EndRunResponse,
      applicationUrl: String,
    ): RunSummaryDto =
      RunSummaryDto(
        runUrl = "$applicationUrl/runs/${response.run.linkId}",
        status = "success",
      )
  }
}
