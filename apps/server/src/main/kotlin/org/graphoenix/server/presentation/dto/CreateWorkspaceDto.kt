package org.graphoenix.server.presentation.dto

import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceRequest

data class CreateWorkspaceDto(
  val orgId: String,
  val name: String,
) {
  fun toRequest() = CreateWorkspaceRequest(orgId = OrganizationId(orgId), name = name)
}
