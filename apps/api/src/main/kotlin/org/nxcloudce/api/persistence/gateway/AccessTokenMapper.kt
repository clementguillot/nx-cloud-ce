package org.nxcloudce.api.persistence.gateway

import org.nxcloudce.api.domain.workspace.model.*
import org.nxcloudce.api.persistence.entity.AccessTokenEntity

fun AccessTokenEntity.toDomain() =
  AccessToken.Builder()
    .id(AccessTokenId(id.toString()))
    .name(name)
    .publicId(AccessTokenPublicId(publicId))
    .accessLevel(AccessLevel.from(accessLevel))
    .workspaceId(WorkspaceId(workspaceId.toString()))
    .encodedValue(encodedValue)
    .build()
