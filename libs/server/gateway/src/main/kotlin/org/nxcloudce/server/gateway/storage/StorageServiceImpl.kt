package org.nxcloudce.server.gateway.storage

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import org.nxcloudce.server.domain.run.gateway.StorageService
import org.nxcloudce.server.domain.run.model.ArtifactId
import org.nxcloudce.server.domain.workspace.model.WorkspaceId
import org.nxcloudce.server.storage.core.FileRepository

@ApplicationScoped
class StorageServiceImpl(
  private val fileRepository: Instance<FileRepository>,
) : StorageService {
  override suspend fun generateGetUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String = fileRepository.get().generateGetUrl("${workspaceId.value}/${artifactId.value}")

  override suspend fun generatePutUrl(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ): String = fileRepository.get().generatePutUrl("${workspaceId.value}/${artifactId.value}")

  override suspend fun deleteArtifact(
    artifactId: ArtifactId,
    workspaceId: WorkspaceId,
  ) = fileRepository.get().deleteFile("${workspaceId.value}/${artifactId.value}")
}
