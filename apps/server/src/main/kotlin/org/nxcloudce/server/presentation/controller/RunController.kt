package org.nxcloudce.server.presentation.controller

import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.server.domain.run.model.Hash
import org.nxcloudce.server.domain.run.usecase.EndRun
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.run.usecase.StartRun
import org.nxcloudce.server.domain.run.usecase.StartRunRequest
import org.nxcloudce.server.domain.workspace.model.AccessLevel
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.presentation.dto.RemoteArtifactListDto
import org.nxcloudce.server.presentation.dto.RunDto
import org.nxcloudce.server.presentation.dto.RunSummaryDto
import org.nxcloudce.server.technical.GzipJsonDecoder
import org.nxcloudce.server.technical.ServerConfiguration
import org.nxcloudce.server.technical.getWorkspaceId

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class RunController(
  private val gzipJsonDecoder: GzipJsonDecoder,
  private val identity: CurrentIdentityAssociation,
  private val serverConfiguration: ServerConfiguration,
  private val startRun: StartRun,
  private val endRun: EndRun,
) {
  @Operation(
    summary = "Retrieves URLs for retrieving/storing cached artifacts",
  )
  @POST
  @Path("/v2/runs/start")
  @Consumes(MediaType.APPLICATION_JSON)
  suspend fun start(startRunDto: RunDto.Start): RemoteArtifactListDto =
    startRun(
      StartRunRequest(
        hashes = startRunDto.hashes.map { Hash(it) },
        workspaceId = WorkspaceId(identity.deferredIdentity.awaitSuspending().principal.name),
        canPut = isReadWriteContext(),
      ),
    ) { response ->
      RemoteArtifactListDto.from(response)
    }

  @Operation(
    summary = "Stores information about a run",
  )
  @POST
  @Path("/runs/end")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  suspend fun end(request: ByteArray): RunSummaryDto =
    gzipJsonDecoder.from(request, RunDto.End::class)
      .let { dto ->
        endRun(
          EndRunRequest(
            run = dto.toRunRequest(),
            tasks = dto.toTaskRequests(),
            workspaceId = identity.getWorkspaceId(),
          ),
        ) { response ->
          RunSummaryDto.from(response, serverConfiguration.applicationUrl())
        }
      }

  @Operation(
    summary = "Indicates if the workspace for the authentication token is enabled",
  )
  @GET
  @Path("/runs/workspace-status")
  @Produces(MediaType.TEXT_PLAIN)
  suspend fun workspaceStatus() = "" // hopefully, all authenticated workspaces are enabled :)

  private suspend fun isReadWriteContext(): Boolean = identity.deferredIdentity.awaitSuspending().hasRole(AccessLevel.READ_WRITE.value)
}
