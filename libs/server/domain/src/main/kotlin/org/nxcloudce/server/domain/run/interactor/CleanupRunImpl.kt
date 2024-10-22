package org.nxcloudce.server.domain.run.interactor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jboss.logging.Logger
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
  companion object {
    private val logger = Logger.getLogger(CleanupRunImpl::class.java)
  }

  override suspend fun <T> invoke(
    request: CleanupRunRequest,
    presenter: (CleanupRunResponse) -> T,
  ): T {
    var deletedRunCount = 0

    runRepository.findAllByCreationDateOlderThan(request.creationDateThreshold).collect { run ->
      logger.info("Removing run ${run.id}")

      taskRepository.findAllByRunId(run.id).collect { task ->
        task.artifactId?.let { artifactId ->
          coroutineScope {
            launch { storageService.deleteArtifact(artifactId, run.workspaceId) }
            launch { artifactRepository.delete(artifactId) }
          }
        }
      }

      logger.info("Artifacts and their files have been deleted")

      coroutineScope {
        launch { taskRepository.deleteAllByRunId(run.id) }
        launch { runRepository.delete(run) }
      }

      logger.info("Tasks and run have been deleted")
      deletedRunCount++
    }

    return presenter(CleanupRunResponse(deletedCount = deletedRunCount))
  }
}
