package org.nxcloudce.server.persistence.gateway

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.gateway.RunRepository
import org.nxcloudce.server.domain.run.model.Run
import org.nxcloudce.server.domain.run.model.RunStatus
import org.nxcloudce.server.domain.run.usecase.EndRunRequest
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.persistence.repository.RunPanacheRepository
import java.time.LocalDateTime

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

  override suspend fun findAllByCreationDateOlderThan(date: LocalDateTime): Collection<Run> =
    runPanacheRepository.findAllByEndTimeLowerThan(date).awaitSuspending().map {
      it.toDomain()
    }

  override suspend fun delete(run: Run) = runPanacheRepository.deleteById(ObjectId(run.id.value)).awaitSuspending()
}
