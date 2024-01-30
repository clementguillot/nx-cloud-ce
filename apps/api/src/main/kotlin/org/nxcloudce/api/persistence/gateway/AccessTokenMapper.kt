package org.nxcloudce.api.persistence.gateway

import org.nxcloudce.api.domain.workspace.model.*
import org.nxcloudce.api.persistence.entity.AccessTokenEntity

fun AccessTokenEntity.toDomain() =
  AccessToken {
    id = AccessTokenId(this@toDomain.id.toString())
    name = this@toDomain.name
    publicId = AccessTokenPublicId(this@toDomain.publicId)
    accessLevel = AccessLevel.from(this@toDomain.accessLevel)
    workspaceId = WorkspaceId(this@toDomain.workspaceId.toString())
    encodedValue = this@toDomain.encodedValue
  }
