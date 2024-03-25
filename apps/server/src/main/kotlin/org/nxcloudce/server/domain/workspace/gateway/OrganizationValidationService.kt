package org.nxcloudce.server.domain.workspace.gateway

import org.nxcloudce.server.domain.organization.model.OrganizationId

fun interface OrganizationValidationService {
  suspend fun isValidOrgId(id: OrganizationId): Boolean
}
