package org.nxcloudce.server.domain.organization.gateway

import org.nxcloudce.server.domain.organization.model.Organization
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest

fun interface OrganizationRepository {
  suspend fun create(org: CreateOrganizationRequest): Organization
}
