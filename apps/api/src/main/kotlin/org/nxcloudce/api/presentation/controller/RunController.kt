package org.nxcloudce.api.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.*
import org.eclipse.microprofile.openapi.annotations.Operation
import org.nxcloudce.api.domain.run.model.Hash
import org.nxcloudce.api.domain.run.usecase.EndRun
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.run.usecase.StartRun
import org.nxcloudce.api.domain.run.usecase.StartRunRequest
import org.nxcloudce.api.domain.workspace.model.AccessLevel
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.presentation.dto.RemoteArtifactListDto
import org.nxcloudce.api.presentation.dto.RunDto
import org.nxcloudce.api.presentation.dto.RunSummaryDto
import org.nxcloudce.api.presentation.infrastructure.ServerConfiguration
import java.util.zip.GZIPInputStream
import kotlin.text.Charsets.UTF_8

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class RunController(
  private val dispatcher: CoroutineDispatcher,
  private val objectMapper: ObjectMapper,
  private val identity: CurrentIdentityAssociation,
  private val serverConfiguration: ServerConfiguration,
  private val startRun: StartRun,
  private val endRun: EndRun,
) {
  @Operation(
    summary = "Retrieves URLs for retrieving/storing cached artifacts",
  )
  @POST
  @Path("/nx-cloud/v2/runs/start")
  @Consumes(MediaType.APPLICATION_JSON)
  suspend fun start(startRunDto: RunDto.Start): RemoteArtifactListDto =
    startRun.start(
      StartRunRequest(
        hashes = startRunDto.hashes.map { Hash(it) },
        workspaceId = WorkspaceId(identity.deferredIdentity.awaitSuspending().principal.name),
        canPut = isReadWriteContext(),
      ),
    ) { response ->
      RemoteArtifactListDto.from(response)
    }

  @POST
  @Path("/nx-cloud/runs/end")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  suspend fun end(request: ByteArray): RunSummaryDto =
    getEndRunDtoFromRequest(request)
      .let { dto ->
        endRun.end(
          EndRunRequest(
            run = dto.toRunRequest(),
            tasks = dto.toTaskRequests(),
            workspaceId = WorkspaceId(identity.deferredIdentity.awaitSuspending().principal.name),
          ),
        ) { response ->
          RunSummaryDto.from(response, serverConfiguration.applicationUrl())
        }
      }

  private suspend fun isReadWriteContext(): Boolean = identity.deferredIdentity.awaitSuspending().hasRole(AccessLevel.READ_WRITE.value)

  private suspend fun getEndRunDtoFromRequest(request: ByteArray): RunDto.End =
    coroutineScope {
      withContext(dispatcher) {
        request.inputStream().use { byteStream ->
          GZIPInputStream(byteStream).use { gzipStream ->
            gzipStream.bufferedReader(UTF_8).use { reader ->
              reader.readLine().let {
                objectMapper.readValue(it, RunDto.End::class.java)
              }
            }
          }
        }
      }
    }
}
