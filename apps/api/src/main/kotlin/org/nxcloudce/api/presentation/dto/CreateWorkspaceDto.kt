package org.nxcloudce.api.presentation.dto

import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest

data class CreateWorkspaceDto(
  val orgId: String,
  val name: String,
) {
  fun toRequest() = CreateWorkspaceRequest(orgId = OrganizationId(orgId), name = name)
}
