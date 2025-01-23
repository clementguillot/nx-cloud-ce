package org.graphoenix.server.domain.workspace.gateway

import org.graphoenix.server.domain.organization.model.OrganizationId

fun interface OrganizationValidationService {
  suspend fun isValidOrgId(id: OrganizationId): Boolean
}
