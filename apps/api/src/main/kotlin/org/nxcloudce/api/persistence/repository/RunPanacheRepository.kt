package org.nxcloudce.api.persistence.repository

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.nxcloudce.api.persistence.entity.RunEntity

@ApplicationScoped
class RunPanacheRepository : ReactivePanacheMongoRepository<RunEntity>
