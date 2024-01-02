package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.persistence.repository.WorkspacePanacheRepository

@ApplicationScoped
class WorkspaceImplWorkspace(private val workspacePanacheRepository: WorkspacePanacheRepository) : WorkspaceRepository {
  override suspend fun create(workspace: CreateWorkspaceRequest): Workspace {
    val entity = workspace.toEntity()

    return workspacePanacheRepository.persist(entity).awaitSuspending().run {
      entity.toDomain()
    }
  }
}
