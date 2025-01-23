package org.graphoenix.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId

@MongoEntity(collection = "access_token")
data class AccessTokenEntity(
  var id: ObjectId?,
  var name: String,
  var publicId: String,
  var accessLevel: String,
  var workspaceId: ObjectId,
  var encodedValue: String,
)
