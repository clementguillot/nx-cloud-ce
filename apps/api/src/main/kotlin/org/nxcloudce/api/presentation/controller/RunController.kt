package org.nxcloudce.api.presentation.controller

import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.run.usecase.StartRun
import org.nxcloudce.api.domain.run.usecase.StartRunRequest
import org.nxcloudce.api.domain.workspace.model.AccessLevel
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.presentation.dto.RemoteArtifactListDto
import org.nxcloudce.api.presentation.dto.StartRunDto

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class RunController(
  private val identity: CurrentIdentityAssociation,
  private val startRun: StartRun,
) {
  @Operation(
    summary = "Retrieves URLs for retrieving/storing cached artifacts",
  )
  @POST
  @Path("/nx-cloud/v2/runs/start")
  @Consumes(MediaType.APPLICATION_JSON)
  suspend fun start(startRunDto: StartRunDto): RemoteArtifactListDto =
    startRun.start(
      StartRunRequest(
        hashes = startRunDto.hashes.map { Hash(it) },
        workspaceId = WorkspaceId(identity.deferredIdentity.awaitSuspending().principal.name),
        canPut = isReadWriteContext(),
      ),
    ) { response -> RemoteArtifactListDto.from(response) }

  private suspend fun isReadWriteContext(): Boolean =
    identity
      .deferredIdentity
      .awaitSuspending()
      .hasRole(AccessLevel.READ_WRITE.value)
}
