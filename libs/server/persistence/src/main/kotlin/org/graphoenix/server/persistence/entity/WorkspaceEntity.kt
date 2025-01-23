package org.graphoenix.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import java.time.LocalDateTime

@MongoEntity(collection = "workspace")
data class WorkspaceEntity(
  var id: ObjectId?,
  var orgId: ObjectId,
  var name: String,
  var installationSource: String?,
  var nxInitDate: LocalDateTime?,
)
