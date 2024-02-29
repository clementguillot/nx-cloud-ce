package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.usecase.CreateOrgAndWorkspaceRequest
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.persistence.entity.WorkspaceEntity
import org.nxcloudce.api.persistence.repository.WorkspacePanacheRepository

@ApplicationScoped
class WorkspaceRepositoryImpl(private val workspacePanacheRepository: WorkspacePanacheRepository) :
  WorkspaceRepository {
  override suspend fun create(workspace: CreateWorkspaceRequest): Workspace {
    val entity = workspace.toEntity()

    return workspacePanacheRepository.persist(entity).awaitSuspending().run { entity.toDomain() }
  }

  override fun create(
    request: CreateOrgAndWorkspaceRequest,
    orgId: OrganizationId,
  ): Uni<Workspace> {
    val entity = WorkspaceEntity(null, ObjectId(orgId.value), request.workspaceName, request.installationSource, request.nxInitDate)
    return workspacePanacheRepository.persist(entity).onItem().transform { it.toDomain() }
  }
}
