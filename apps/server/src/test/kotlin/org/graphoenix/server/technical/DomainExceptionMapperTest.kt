package org.graphoenix.server.technical

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.exception.OrganizationNotFoundException
import org.junit.jupiter.api.Test

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
