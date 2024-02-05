package org.nxcloudce.api.domain.run.interactor

import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.run.gateway.ArtifactRepository
import org.nxcloudce.api.domain.run.gateway.RunRepository
import org.nxcloudce.api.domain.run.gateway.TaskRepository
import org.nxcloudce.api.domain.run.model.Artifact
import org.nxcloudce.api.domain.run.model.RunStatus
import org.nxcloudce.api.domain.run.model.Task
import org.nxcloudce.api.domain.run.usecase.EndRun
import org.nxcloudce.api.domain.run.usecase.EndRunRequest
import org.nxcloudce.api.domain.run.usecase.EndRunResponse
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

@ApplicationScoped
class EndRunImpl(
  private val runRepository: RunRepository,
  private val taskRepository: TaskRepository,
  private val artifactRepository: ArtifactRepository,
) : EndRun {
  override suspend fun <T> end(
    request: EndRunRequest,
    presenter: (EndRunResponse) -> T,
  ): T {
    val run = runRepository.create(request.run, getRunStatus(request.tasks), request.workspaceId)
    val tasks = taskRepository.create(request.tasks, run.id, request.workspaceId)

    createTaskArtifacts(tasks, request.workspaceId)

    return presenter(
      EndRunResponse(run = run),
    )
  }

  private fun getRunStatus(tasks: Collection<EndRunRequest.Task>): RunStatus =
    when (tasks.any { it.status != 0 }) {
      true -> RunStatus.FAILURE
      false -> RunStatus.SUCCESS
    }

  private suspend fun createTaskArtifacts(
    tasks: Collection<Task>,
    workspaceId: WorkspaceId,
  ): Collection<Artifact.Exist> =
    tasks.filter { it.uploadedToStorage }
      .map { mapOf(it.artifactId!! to it.hash) }
      .flatMap { artifactRepository.createRemoteArtifacts(it, workspaceId) }
}
