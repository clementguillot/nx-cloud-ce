package org.nxcloudce.api.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId

@MongoEntity(collection = "artifact")
data class ArtifactEntity(
  var id: ObjectId?,
  var artifactId: String,
  var hash: String,
  var workspaceId: ObjectId,
)
