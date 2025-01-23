package org.graphoenix.server.domain.organization.interactor

import org.graphoenix.server.domain.organization.gateway.OrganizationRepository
import org.graphoenix.server.domain.organization.usecase.CreateOrganization
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationResponse

class CreateOrganizationImpl(
  private val orgGateway: OrganizationRepository,
) : CreateOrganization {
  override suspend operator fun <T> invoke(
    request: CreateOrganizationRequest,
    presenter: (CreateOrganizationResponse) -> T,
  ): T {
    val org = orgGateway.create(request)
    return presenter(CreateOrganizationResponse(org))
  }
}
