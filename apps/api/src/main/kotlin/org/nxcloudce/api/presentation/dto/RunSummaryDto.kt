package org.nxcloudce.api.presentation.dto

import io.quarkus.runtime.annotations.RegisterForReflection
import org.nxcloudce.api.domain.run.usecase.EndRunResponse

@RegisterForReflection
data class RunSummaryDto(
  val runUrl: String,
  val status: String,
) {
  companion object {
    fun from(response: EndRunResponse): RunSummaryDto =
      RunSummaryDto(
        runUrl = "http://TBD/runs/${response.run.linkId}",
        status = "success",
      )
  }
}
