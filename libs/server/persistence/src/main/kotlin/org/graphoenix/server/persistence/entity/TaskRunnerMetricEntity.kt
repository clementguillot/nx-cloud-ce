package org.graphoenix.server.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import java.time.LocalDateTime

@MongoEntity(collection = "task_runner_metric")
data class TaskRunnerMetricEntity(
  var id: ObjectId?,
  var workspaceId: ObjectId,
  var recordingDate: LocalDateTime,
  var durationMs: Int,
  var success: Boolean,
  var statusCode: Int,
  var entryType: String,
  var payloadSize: Long?,
)
