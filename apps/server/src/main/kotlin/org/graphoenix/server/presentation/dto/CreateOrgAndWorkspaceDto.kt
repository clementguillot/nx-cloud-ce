package org.graphoenix.server.presentation.dto

import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import java.time.LocalDateTime

data class CreateOrgAndWorkspaceDto(
  val workspaceName: String,
  val installationSource: String,
  val nxInitDate: LocalDateTime?,
) {
  fun toRequest() =
    CreateOrgAndWorkspaceRequest(
      workspaceName = workspaceName,
      installationSource = installationSource,
      nxInitDate = nxInitDate,
    )
}
