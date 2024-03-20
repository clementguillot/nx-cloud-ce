package org.nxcloudce.api.presentation.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery
import org.nxcloudce.api.presentation.dto.VerifyClientBundleDto
import org.nxcloudce.api.presentation.infrastructure.ServerConfiguration

@Path("/client/verify")
class ClientController(private val serverConfiguration: ServerConfiguration) {
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
