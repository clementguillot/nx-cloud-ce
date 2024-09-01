package org.nxcloudce.server.storage.gateway

import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.storage.core.FileRepository

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

  override suspend fun deleteArtifact(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ) = fileRepository.deleteFile("${workspaceId.value}/${artifactId.value}")
}
