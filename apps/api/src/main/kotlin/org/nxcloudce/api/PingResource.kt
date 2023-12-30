package org.nxcloudce.api

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation

@Path("/ping")
class PingResource {
  @Operation(
    description = "Test api",
  )
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  fun ping() = ""
}
