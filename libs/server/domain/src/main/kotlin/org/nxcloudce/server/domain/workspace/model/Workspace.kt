package org.nxcloudce.server.domain.workspace.model

import org.nxcloudce.server.domain.organization.model.OrganizationId
import java.time.LocalDateTime

@JvmInline value class WorkspaceId(
  val value: String,
)

data class Workspace(
  val id: WorkspaceId,
  val orgId: OrganizationId,
  val name: String,
  val installationSource: String?,
  val nxInitDate: LocalDateTime?,
)
