package org.graphoenix.server.domain.organization.usecase

import org.graphoenix.server.domain.UseCase
import org.graphoenix.server.domain.organization.model.Organization

interface CreateOrganization : UseCase<CreateOrganizationRequest, CreateOrganizationResponse>

data class CreateOrganizationRequest(
  val name: String,
)

data class CreateOrganizationResponse(
  val organization: Organization,
)
