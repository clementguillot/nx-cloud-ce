package org.nxcloudce.server.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.server.persistence.entity.RunEntity

@ApplicationScoped
class RunPanacheRepository : ReactivePanacheMongoRepository<RunEntity>
