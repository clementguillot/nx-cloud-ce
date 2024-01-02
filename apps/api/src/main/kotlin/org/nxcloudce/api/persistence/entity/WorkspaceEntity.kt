package org.nxcloudce.api.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId

@MongoEntity(collection = "workspace")
data class WorkspaceEntity(
  var id: ObjectId?,
  var orgId: ObjectId,
  var name: String,
)
