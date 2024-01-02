package org.nxcloudce.api.presentation.infrastructure

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.exception.OrganizationNotFoundException
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class DomainExceptionMapperTest {
  @Inject
  lateinit var domainExceptionMapper: DomainExceptionMapper

  @Test
  fun `should return 400 Bad Request on OrganizationNotFoundException`() {
    val dummyException = OrganizationNotFoundException(OrganizationId("dummy-id"))

    val result = domainExceptionMapper.toResponse(dummyException)

    assertEquals(400, result.status)
  }
}
