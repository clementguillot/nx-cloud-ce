package org.nxcloudce.api.presentation.infrastructure

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.nxcloudce.api.domain.workspace.exception.OrganizationNotFoundException

@Provider
class DomainExceptionMapper : ExceptionMapper<OrganizationNotFoundException> {
  override fun toResponse(exception: OrganizationNotFoundException) = Response.status(Response.Status.BAD_REQUEST).build()
}
