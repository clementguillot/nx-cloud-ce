package org.nxcloudce.server.presentation.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.openapi.annotations.Operation
import org.jboss.resteasy.reactive.RestQuery
import org.nxcloudce.server.presentation.dto.VerifyClientBundleDto
import org.nxcloudce.server.presentation.infrastructure.ServerConfiguration

@Path("/client/verify")
class ClientController(private val serverConfiguration: ServerConfiguration) {
  @Operation(
    summary = "Verifies the version of the `nx-cloud-client-bundle`, returns a download URL if needed",
  )
  @GET
  suspend fun verify(
    @RestQuery version: String?,
    @RestQuery contentHash: String?,
  ): VerifyClientBundleDto {
    if (!version.isNullOrEmpty() && version == serverConfiguration.clientBundleVersion()) {
      return VerifyClientBundleDto.ValidVerifyClientBundleDto()
    }
    return VerifyClientBundleDto.InvalidVerifyClientBundleDto(
      url = "${serverConfiguration.applicationUrl()}/${serverConfiguration.clientBundlePath()}",
      version = serverConfiguration.clientBundleVersion(),
    )
  }
}
