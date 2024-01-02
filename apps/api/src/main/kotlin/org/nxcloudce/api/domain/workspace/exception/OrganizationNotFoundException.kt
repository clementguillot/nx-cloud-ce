package org.nxcloudce.api.domain.workspace.exception

import org.nxcloudce.api.domain.organization.model.OrganizationId

class OrganizationNotFoundException(orgId: OrganizationId) :
  RuntimeException("Organization ID '${orgId.value}' not found")
