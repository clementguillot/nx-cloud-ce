package org.nxcloudce.api.presentation.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.RestQuery
import org.nxcloudce.api.presentation.dto.VerifyClientBundleDto

@Path("/nx-cloud/client/verify")
class ClientController {
  @ConfigProperty(name = "nx-server.application-url")
  lateinit var nxApplicationUrl: String

  @ConfigProperty(name = "nx-server.client-bundle-version")
  lateinit var nxClientBundleVersion: String

  @ConfigProperty(name = "nx-server.client-bundle-path")
  lateinit var nxClientBundlePath: String

  @GET
  fun verify(
    @RestQuery version: String?,
    @RestQuery contentHash: String?,
  ): VerifyClientBundleDto {
    if (!version.isNullOrEmpty() && version == nxClientBundleVersion) {
      return VerifyClientBundleDto.ValidVerifyClientBundleDto()
    }
    return VerifyClientBundleDto.InvalidVerifyClientBundleDto(
      url = "$nxApplicationUrl/$nxClientBundlePath",
      version = nxClientBundleVersion,
    )
  }
}
