package org.nxcloudce.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import org.nxcloudce.server.domain.run.model.HashDetails
import java.time.LocalDateTime

@MongoEntity(collection = "task")
data class TaskEntity(
  var id: ObjectId?,
  var runId: ObjectId,
  var workspaceId: ObjectId,
  var taskId: String,
  var hash: String,
  var projectName: String,
  var target: String,
  var startTime: LocalDateTime,
  var endTime: LocalDateTime,
  var cacheStatus: String,
  var status: Int,
  var uploadedToStorage: Boolean,
  var params: String,
  var terminalOutput: String,
  var hashDetails: HashDetails,
  var artifactId: String?,
)
