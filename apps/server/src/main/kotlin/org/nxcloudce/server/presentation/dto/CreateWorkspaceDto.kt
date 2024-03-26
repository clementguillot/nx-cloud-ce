package org.nxcloudce.server.presentation.dto

import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspaceRequest

data class CreateWorkspaceDto(
  val orgId: String,
  val name: String,
) {
  fun toRequest() = CreateWorkspaceRequest(orgId = OrganizationId(orgId), name = name)
}
