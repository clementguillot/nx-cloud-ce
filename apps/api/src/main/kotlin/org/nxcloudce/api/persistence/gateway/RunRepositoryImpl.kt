package org.nxcloudce.api.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.run.gateway.RunRepository
import org.nxcloudce.api.domain.run.model.Run
import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.workspace.model.WorkspaceId
import org.nxcloudce.api.persistence.repository.RunPanacheRepository

@ApplicationScoped
class RunRepositoryImpl(
  private val runPanacheRepository: RunPanacheRepository,
) : RunRepository {
  override suspend fun create(
    run: EndRunRequest.Run,
    status: RunStatus,
    workspaceId: WorkspaceId,
  ): Run {
    val entity = run.toEntity(status, workspaceId)

    return runPanacheRepository.persist(entity).awaitSuspending().run { entity.toDomain() }
  }
}
