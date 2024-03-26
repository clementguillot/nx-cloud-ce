package org.nxcloudce.server.domain.organization.usecase

import org.nxcloudce.server.domain.UseCase
import org.nxcloudce.server.domain.organization.model.Organization

interface CreateOrganization : UseCase<CreateOrganizationRequest, CreateOrganizationResponse>

data class CreateOrganizationRequest(val name: String)

data class CreateOrganizationResponse(val organization: Organization)
