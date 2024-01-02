package org.nxcloudce.api.presentation.dto

import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest

data class CreateOrganizationDto(
  val name: String,
) {
  fun toRequest() = CreateOrganizationRequest(name = name)
}
