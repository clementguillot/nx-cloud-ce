package org.graphoenix.server.domain.workspace.exception

import org.graphoenix.server.domain.organization.model.OrganizationId

class OrganizationNotFoundException(
  orgId: OrganizationId,
) : RuntimeException("Organization ID '${orgId.value}' not found")
