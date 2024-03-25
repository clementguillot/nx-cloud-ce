package org.nxcloudce.server.domain.workspace.exception

import org.nxcloudce.server.domain.organization.model.OrganizationId

class OrganizationNotFoundException(orgId: OrganizationId) :
  RuntimeException("Organization ID '${orgId.value}' not found")
