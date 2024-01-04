package org.nxcloudce.api.presentation.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspace
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspace
import org.nxcloudce.api.presentation.dto.CreateOrgAndWorkspaceDto
import org.nxcloudce.api.presentation.dto.CreateWorkspaceDto
import org.nxcloudce.api.presentation.dto.IdDto
import org.nxcloudce.api.presentation.dto.InitWorkspaceDto

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class WorkspaceController(
  private val createWorkspace: CreateWorkspace,
  private val createOrgAndWorkspace: CreateOrgAndWorkspace,
) {
  @Operation(summary = "Create a public cloud counterpart for a private cloud workspace")
  @POST
  @Path("/nx-cloud/private/create-workspace")
  suspend fun create(workspaceDto: CreateWorkspaceDto) =
    createWorkspace.create(workspaceDto.toRequest()) { response ->
      IdDto(response.workspace.id.value)
    }

  @Operation(summary = "Create an org and a workspace")
  @POST
  @Path("/nx-cloud/create-org-and-workspace")
  fun createOrgAndWorkspace(requestDto: CreateOrgAndWorkspaceDto) =
    createOrgAndWorkspace.create(
      requestDto.toRequest(),
    ) { response ->
      response.onItem().transform {
        InitWorkspaceDto(
          url = "http://TBD?token=${it.accessToken.encodedValue}",
          token = it.accessToken.encodedValue,
        )
      }
    }
}
