package org.nxcloudce.server.domain.run.interactor

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.nxcloudce.server.domain.run.gateway.ArtifactRepository
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.model.Artifact
import org.nxcloudce.server.domain.run.usecase.StartRun
import org.nxcloudce.server.domain.run.usecase.StartRunRequest
import org.nxcloudce.server.domain.run.usecase.StartRunResponse

@ApplicationScoped
class StartRunImpl(
  private val artifactRepository: ArtifactRepository,
  private val storageService: StorageService,
) : StartRun {
  override suspend fun <T> start(
    request: StartRunRequest,
    presenter: (StartRunResponse) -> T,
  ): T {
    val existingArtifacts = artifactRepository.findByHash(request.hashes, request.workspaceId)
    val existingHashes = existingArtifacts.map { it.hash }
    val newArtifacts =
      artifactRepository.createWithHash(
        request.hashes.filter { it !in existingHashes },
        request.workspaceId,
      )
    val artifacts = existingArtifacts + newArtifacts

    coroutineScope {
      artifacts.map { artifact ->
        async {
          updateArtifactUrls(artifact, request.canPut)
        }
      }
    }.awaitAll()

    return presenter(StartRunResponse(artifacts = artifacts))
  }

  private suspend fun updateArtifactUrls(
    artifact: Artifact,
    canPut: Boolean,
  ) {
    if (artifact is Artifact.Exist) {
      artifact.setGetUrl(storageService)
    }
    if (canPut) {
      artifact.setPutUrl(storageService)
    }
  }
}
