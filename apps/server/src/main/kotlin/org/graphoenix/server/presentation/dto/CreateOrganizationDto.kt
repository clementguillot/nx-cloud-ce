package org.graphoenix.server.presentation.dto

import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest

data class CreateOrganizationDto(
  val name: String,
) {
  fun toRequest() = CreateOrganizationRequest(name = name)
}
