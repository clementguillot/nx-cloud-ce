package org.nxcloudce.api.domain.organization.usecase

import org.nxcloudce.api.domain.organization.model.Organization

interface CreateOrganization {
  suspend fun <T> create(
    request: CreateOrganizationRequest,
    presenter: (CreateOrganizationResponse) -> T,
  ): T
}

data class CreateOrganizationRequest(val name: String)

data class CreateOrganizationResponse(val organization: Organization)
