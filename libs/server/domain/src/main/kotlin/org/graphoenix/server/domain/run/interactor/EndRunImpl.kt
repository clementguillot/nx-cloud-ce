package org.graphoenix.server.domain.run.interactor

import org.graphoenix.server.domain.run.gateway.ArtifactRepository
import org.graphoenix.server.domain.run.gateway.RunRepository
import org.graphoenix.server.domain.run.gateway.TaskRepository
import org.graphoenix.server.domain.run.model.Artifact
import org.graphoenix.server.domain.run.model.RunStatus
import org.graphoenix.server.domain.run.model.Task
import org.graphoenix.server.domain.run.usecase.EndRun
import org.graphoenix.server.domain.run.usecase.EndRunRequest
import org.graphoenix.server.domain.run.usecase.EndRunResponse
import org.graphoenix.server.domain.workspace.model.WorkspaceId

class EndRunImpl(
  private val runRepository: RunRepository,
  private val taskRepository: TaskRepository,
  private val artifactRepository: ArtifactRepository,
) : EndRun {
  override suspend operator fun <T> invoke(
    request: EndRunRequest,
    presenter: (EndRunResponse) -> T,
  ): T {
    val run = runRepository.create(request.run, getRunStatus(request.tasks), request.workspaceId)
    val tasks = taskRepository.create(request.tasks, run.id, request.workspaceId)

    createTaskArtifacts(tasks, request.workspaceId)

    return presenter(EndRunResponse(run = run))
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
    tasks
      .filter { it.uploadedToStorage }
      .map { mapOf(it.artifactId!! to it.hash) }
      .flatMap { artifactRepository.createRemoteArtifacts(it, workspaceId) }
}
