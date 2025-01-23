package org.graphoenix.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.graphoenix.server.persistence.entity.WorkspaceEntity

@ApplicationScoped
class WorkspacePanacheRepository : ReactivePanacheMongoRepository<WorkspaceEntity>
