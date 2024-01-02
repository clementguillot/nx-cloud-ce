package org.nxcloudce.api.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.persistence.entity.WorkspaceEntity

@ApplicationScoped
class WorkspacePanacheRepository : ReactivePanacheMongoRepository<WorkspaceEntity>
