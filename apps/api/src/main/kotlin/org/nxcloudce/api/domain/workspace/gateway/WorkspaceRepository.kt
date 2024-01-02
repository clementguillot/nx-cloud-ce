package org.nxcloudce.api.domain.workspace.gateway

import org.nxcloudce.api.domain.workspace.model.Workspace
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest

fun interface WorkspaceRepository {
  suspend fun create(workspace: CreateWorkspaceRequest): Workspace
}
