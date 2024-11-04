package org.nxcloudce.server.presentation.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.server.domain.organization.usecase.CreateOrganization
import org.nxcloudce.server.presentation.dto.CreateOrganizationDto
import org.nxcloudce.server.presentation.dto.IdDto

@Path("/private/create-org")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class OrganizationController(
  private val createOrganization: CreateOrganization,
) {
  @Operation(
    summary = "Create a public cloud counterpart for a private cloud org",
  )
  @POST
  suspend fun create(orgDto: CreateOrganizationDto) =
    createOrganization(orgDto.toRequest()) { response ->
      IdDto(response.organization.id.value)
    }
}
