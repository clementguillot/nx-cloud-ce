package org.graphoenix.server.presentation.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspace
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspace
import org.graphoenix.server.presentation.dto.CreateOrgAndWorkspaceDto
import org.graphoenix.server.presentation.dto.CreateWorkspaceDto
import org.graphoenix.server.presentation.dto.IdDto
import org.graphoenix.server.presentation.dto.InitWorkspaceDto
import org.graphoenix.server.technical.ServerConfiguration

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class WorkspaceController(
  private val serverConfiguration: ServerConfiguration,
  private val createWorkspace: CreateWorkspace,
  private val createOrgAndWorkspace: CreateOrgAndWorkspace,
) {
  @Operation(summary = "Create a public cloud counterpart for a private cloud workspace")
  @POST
  @Path("/private/create-workspace")
  suspend fun create(workspaceDto: CreateWorkspaceDto) =
    createWorkspace(workspaceDto.toRequest()) { response ->
      IdDto(response.workspace.id.value)
    }

  @Operation(summary = "Create an org and a workspace")
  @POST
  @Path("/create-org-and-workspace")
  fun createOrgAndWorkspace(requestDto: CreateOrgAndWorkspaceDto) =
    createOrgAndWorkspace(
      requestDto.toRequest(),
    ) { response ->
      response.onItem().transform {
        InitWorkspaceDto(
          url = "${serverConfiguration.applicationUrl()}?token=${it.accessToken.encodedValue}",
          token = it.accessToken.encodedValue,
        )
      }
    }
}
