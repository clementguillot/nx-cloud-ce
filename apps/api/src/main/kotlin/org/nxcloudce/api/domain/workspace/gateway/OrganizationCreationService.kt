package org.nxcloudce.api.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.nxcloudce.api.domain.organization.model.Organization

fun interface OrganizationCreationService {
  fun createOrg(orgName: String): Uni<Organization>
}
