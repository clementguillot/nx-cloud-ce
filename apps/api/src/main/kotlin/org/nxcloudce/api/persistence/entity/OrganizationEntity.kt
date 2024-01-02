package org.nxcloudce.api.persistence.entity

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId

@MongoEntity(collection = "organization")
data class OrganizationEntity(var id: ObjectId?, var name: String)
