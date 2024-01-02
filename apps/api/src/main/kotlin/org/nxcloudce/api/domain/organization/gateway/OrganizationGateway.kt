package org.nxcloudce.api.domain.organization.gateway

import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest

fun interface OrganizationGateway {
  suspend fun create(org: CreateOrganizationRequest): Organization
}
