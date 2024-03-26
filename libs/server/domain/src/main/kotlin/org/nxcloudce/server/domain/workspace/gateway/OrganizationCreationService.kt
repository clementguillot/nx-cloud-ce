package org.nxcloudce.server.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.nxcloudce.server.domain.organization.model.Organization

fun interface OrganizationCreationService {
  fun createOrg(orgName: String): Uni<Organization>
}
