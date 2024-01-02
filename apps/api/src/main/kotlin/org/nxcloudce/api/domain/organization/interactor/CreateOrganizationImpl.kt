package org.nxcloudce.api.domain.organization.interactor

import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.api.domain.organization.usecase.CreateOrganization
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationResponse

@ApplicationScoped
class CreateOrganizationImpl(private val orgGateway: OrganizationRepository) : CreateOrganization {
  override suspend fun <T> create(
    request: CreateOrganizationRequest,
    presenter: (CreateOrganizationResponse) -> T,
  ): T {
    val org = orgGateway.create(request)
    return presenter(CreateOrganizationResponse(org))
  }
}
