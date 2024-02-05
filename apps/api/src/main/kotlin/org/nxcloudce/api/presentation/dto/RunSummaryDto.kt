package org.nxcloudce.api.presentation.dto

import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.usecase.EndRunResponse

data class RunSummaryDto(
  val runUrl: String,
  val status: String,
) {
  companion object {
    fun from(response: EndRunResponse): RunSummaryDto =
      RunSummaryDto(
        runUrl = response.run.linkId,
        status =
          when (response.run.status) {
            RunStatus.SUCCESS -> "success"
            RunStatus.FAILURE -> "failure"
          },
      )
  }
}
