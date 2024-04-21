package org.nxcloudce.server.domain.run.usecase

import org.nxcloudce.server.domain.UseCase
import java.time.LocalDateTime

interface CleanupRun : UseCase<CleanupRunRequest, CleanupRunResponse>

data class CleanupRunRequest(val creationDateThreshold: LocalDateTime)

data class CleanupRunResponse(val deletedCount: Int)
