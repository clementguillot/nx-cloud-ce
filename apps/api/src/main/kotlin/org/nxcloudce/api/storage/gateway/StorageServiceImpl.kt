package org.nxcloudce.api.storage.gateway

import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.domain.run.gateway.StorageService
import org.nxcloudce.api.domain.run.model.ArtifactId
import org.nxcloudce.api.domain.workspace.model.WorkspaceId

@ApplicationScoped
class StorageServiceImpl(
  private val fileRepository: FileRepository,
) : StorageService {
  override suspend fun generateGetUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String = fileRepository.generateGetUrl("${workspaceId.value}/${artifactId.value}")

  override suspend fun generatePutUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String = fileRepository.generatePutUrl("${workspaceId.value}/${artifactId.value}")
}
