package org.nxcloudce.api.presentation.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspace
import org.nxcloudce.api.presentation.dto.CreateWorkspaceDto
import org.nxcloudce.api.presentation.dto.IdDto

@Path("/nx-cloud/private/create-workspace")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class WorkspaceController(private val createWorkspace: CreateWorkspace) {
  @Operation(description = "Create a public cloud counterpart for a private cloud workspace")
  @POST
  suspend fun create(workspaceDto: CreateWorkspaceDto) =
    createWorkspace.create(workspaceDto.toRequest()) { response ->
      IdDto(response.workspace.id.value)
    }
}
