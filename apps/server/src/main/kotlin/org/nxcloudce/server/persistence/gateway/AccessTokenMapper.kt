package org.nxcloudce.server.persistence.gateway

import org.nxcloudce.server.domain.workspace.model.*
import org.nxcloudce.server.persistence.entity.AccessTokenEntity

fun AccessTokenEntity.toDomain(): AccessToken =
  AccessToken {
    id = AccessTokenId(this@toDomain.id.toString())
    name = this@toDomain.name
    publicId = AccessTokenPublicId(this@toDomain.publicId)
    accessLevel = AccessLevel.from(this@toDomain.accessLevel)
    workspaceId = WorkspaceId(this@toDomain.workspaceId.toString())
    encodedValue = this@toDomain.encodedValue
  }
