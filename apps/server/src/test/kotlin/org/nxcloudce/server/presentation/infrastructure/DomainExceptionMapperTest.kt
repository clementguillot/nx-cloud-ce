package org.nxcloudce.server.presentation.infrastructure

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.workspace.exception.OrganizationNotFoundException

@QuarkusTest
class DomainExceptionMapperTest {
  @Inject
  lateinit var domainExceptionMapper: DomainExceptionMapper

  @Test
  fun `should return 400 Bad Request on OrganizationNotFoundException`() {
    val dummyException = OrganizationNotFoundException(OrganizationId("dummy-id"))

    val result = domainExceptionMapper.toResponse(dummyException)

    expect(result.status).toEqual(400)
  }
}
