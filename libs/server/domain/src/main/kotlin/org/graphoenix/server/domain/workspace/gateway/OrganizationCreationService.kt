package org.graphoenix.server.domain.workspace.gateway

import io.smallrye.mutiny.Uni
import org.graphoenix.server.domain.organization.model.Organization

fun interface OrganizationCreationService {
  fun createOrg(orgName: String): Uni<Organization>
}
