package org.nxcloudce.server.domain.organization.usecase

import org.nxcloudce.server.domain.organization.model.Organization

interface CreateOrganization {
  suspend fun <T> create(
    request: CreateOrganizationRequest,
    presenter: (CreateOrganizationResponse) -> T,
  ): T
}

data class CreateOrganizationRequest(val name: String)

data class CreateOrganizationResponse(val organization: Organization)
