package org.nxcloudce.api.domain.workspace.gateway

import org.nxcloudce.api.domain.organization.model.OrganizationId

fun interface OrganizationValidationService {
  suspend fun isValidOrgId(id: OrganizationId): Boolean
}
