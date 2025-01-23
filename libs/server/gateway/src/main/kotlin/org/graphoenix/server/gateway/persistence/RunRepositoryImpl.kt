package org.graphoenix.server.gateway.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.bson.types.ObjectId
import org.graphoenix.server.domain.run.gateway.RunRepository
import org.graphoenix.server.domain.run.model.Run
import org.graphoenix.server.domain.run.model.RunStatus
import org.graphoenix.server.domain.run.usecase.EndRunRequest
import org.graphoenix.server.domain.workspace.model.WorkspaceId
import org.graphoenix.server.persistence.repository.RunPanacheRepository
import java.time.LocalDateTime

@ApplicationScoped
class RunRepositoryImpl(
  private val runPanacheRepository: RunPanacheRepository,
  private val objectMapper: ObjectMapper,
) : RunRepository {
  override suspend fun create(
    run: EndRunRequest.Run,
    status: RunStatus,
    workspaceId: WorkspaceId,
  ): Run {
    val entity = run.toEntity(status, workspaceId, objectMapper)

    return runPanacheRepository.persist(entity).awaitSuspending().run { entity.toDomain(objectMapper) }
  }

  override fun findAllByCreationDateOlderThan(date: LocalDateTime): Flow<Run> =
    runPanacheRepository.findAllByEndTimeLowerThan(date).asFlow().map { it.toDomain(objectMapper) }

  override suspend fun delete(run: Run) = runPanacheRepository.deleteById(ObjectId(run.id.value)).awaitSuspending()
}
