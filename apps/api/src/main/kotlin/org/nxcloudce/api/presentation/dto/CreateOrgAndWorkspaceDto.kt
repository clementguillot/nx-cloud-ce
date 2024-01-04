package org.nxcloudce.api.presentation.dto

import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspaceRequest

data class CreateOrgAndWorkspaceDto(
  val workspaceName: String,
  val installationSource: String,
) {
  fun toRequest() =
    CreateOrgAndWorkspaceRequest(
      workspaceName = workspaceName,
      installationSource = installationSource,
    )
}
