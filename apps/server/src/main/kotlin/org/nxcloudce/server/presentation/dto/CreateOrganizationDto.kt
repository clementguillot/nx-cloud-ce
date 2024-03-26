package org.nxcloudce.server.presentation.dto

import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest

data class CreateOrganizationDto(
  val name: String,
) {
  fun toRequest() = CreateOrganizationRequest(name = name)
}
