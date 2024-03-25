package org.nxcloudce.server.domain.organization.interactor

import org.nxcloudce.server.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.server.domain.organization.usecase.CreateOrganization
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationResponse

class CreateOrganizationImpl(private val orgGateway: OrganizationRepository) : CreateOrganization {
  override suspend fun <T> create(
    request: CreateOrganizationRequest,
    presenter: (CreateOrganizationResponse) -> T,
  ): T {
    val org = orgGateway.create(request)
    return presenter(CreateOrganizationResponse(org))
  }
}
