package org.nxcloudce.server.domain.run.interactor

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.nxcloudce.server.domain.run.gateway.ArtifactRepository
import org.nxcloudce.server.domain.run.gateway.RunRepository
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.gateway.TaskRepository
import org.nxcloudce.server.domain.run.usecase.CleanupRun
import org.nxcloudce.server.domain.run.usecase.CleanupRunRequest
import org.nxcloudce.server.domain.run.usecase.CleanupRunResponse

class CleanupRunImpl(
  private val runRepository: RunRepository,
  private val taskRepository: TaskRepository,
  private val artifactRepository: ArtifactRepository,
  private val storageService: StorageService,
) : CleanupRun {
  override suspend fun <T> invoke(
    request: CleanupRunRequest,
    presenter: (CleanupRunResponse) -> T,
  ): T {
    val runs = runRepository.findAllByCreationDateOlderThan(request.creationDateThreshold)

    runs.forEach { run ->
      val tasks = taskRepository.findAllByRunId(run.id)
      val artifacts =
        artifactRepository.findByHash(
          tasks.filter { it.artifactId != null }.map { it.hash },
          run.workspaceId,
        )

      coroutineScope {
        artifacts.map { artifact ->
          async {
            Pair(
              storageService.deleteArtifact(artifact.id, run.workspaceId),
              artifactRepository.delete(artifact),
            )
          }
        }
      }
        .awaitAll()
      coroutineScope {
        launch { taskRepository.deleteAllByRunId(run.id) }
        launch { runRepository.delete(run) }
      }
    }

    return presenter(CleanupRunResponse(deletedCount = runs.size))
  }
}
