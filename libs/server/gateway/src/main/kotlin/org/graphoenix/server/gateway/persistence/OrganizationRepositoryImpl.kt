package org.graphoenix.server.gateway.persistence

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.graphoenix.server.domain.organization.gateway.OrganizationRepository
import org.graphoenix.server.domain.organization.model.Organization
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest
import org.graphoenix.server.domain.workspace.gateway.OrganizationCreationService
import org.graphoenix.server.domain.workspace.gateway.OrganizationValidationService
import org.graphoenix.server.persistence.repository.OrganizationPanacheRepository

@ApplicationScoped
class OrganizationRepositoryImpl(
  private val orgPanacheRepository: OrganizationPanacheRepository,
) : OrganizationRepository,
  OrganizationValidationService,
  OrganizationCreationService {
  override suspend fun create(org: CreateOrganizationRequest): Organization {
    val entity = org.toEntity()

    return orgPanacheRepository.persist(entity).awaitSuspending().run { entity.toDomain() }
  }

  override suspend fun isValidOrgId(id: OrganizationId): Boolean =
    orgPanacheRepository.findById(ObjectId(id.value)).awaitSuspending() !== null

  override fun createOrg(orgName: String): Uni<Organization> {
    val entity = CreateOrganizationRequest(orgName).toEntity()
    return orgPanacheRepository.persist(entity).onItem().transform { it.toDomain() }
  }
}
