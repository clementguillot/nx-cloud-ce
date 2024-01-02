package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.gateway.OrganizationRepository
import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.domain.workspace.gateway.OrganizationValidationService
import org.nxcloudce.api.persistence.repository.OrganizationPanacheRepository

@ApplicationScoped
class OrganizationRepositoryImpl(
  private val orgPanacheRepository: OrganizationPanacheRepository,
) :
  OrganizationRepository, OrganizationValidationService {
  override suspend fun create(org: CreateOrganizationRequest): Organization {
    val entity = org.toEntity()

    return orgPanacheRepository.persist(entity).awaitSuspending().run { entity.toDomain() }
  }

  override suspend fun isValidOrgId(id: OrganizationId): Boolean {
    return orgPanacheRepository.findById(ObjectId(id.value)).awaitSuspending() !== null
  }
}
