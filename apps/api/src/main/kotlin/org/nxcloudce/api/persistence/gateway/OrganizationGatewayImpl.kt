package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.organization.gateway.OrganizationGateway
import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.persistence.repository.OrganizationPanacheRepository

@ApplicationScoped
class OrganizationGatewayImpl(private val orgPanacheRepository: OrganizationPanacheRepository) :
  OrganizationGateway {
  override suspend fun create(org: CreateOrganizationRequest): Organization {
    val entity = org.toEntity()

    return orgPanacheRepository.persist(entity).awaitSuspending().run {
      entity.toDomain()
    }
  }
}
