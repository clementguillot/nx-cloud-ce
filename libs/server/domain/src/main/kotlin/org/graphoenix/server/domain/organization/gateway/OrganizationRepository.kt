package org.graphoenix.server.domain.organization.gateway

import org.graphoenix.server.domain.organization.model.Organization
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest

fun interface OrganizationRepository {
  suspend fun create(org: CreateOrganizationRequest): Organization
}
