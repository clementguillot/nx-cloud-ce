package org.nxcloudce.api.domain.workspace.model

import java.util.*

@JvmInline value class AccessTokenId(val value: String)

@JvmInline value class AccessTokenPublicId(val value: String = UUID.randomUUID().toString())

class AccessToken private constructor(builder: Builder) {
  class Builder {
    var id: AccessTokenId? = null
      private set

    var name: String? = null
      private set

    var publicId: AccessTokenPublicId? = null
      private set

    var accessLevel: AccessLevel? = null
      private set

    var workspaceId: WorkspaceId? = null
      private set

    var encodedValue: String? = null
      private set

    fun id(id: AccessTokenId) = apply { this.id = id }

    fun name(name: String) = apply { this.name = name }

    fun publicId(publicId: AccessTokenPublicId) = apply { this.publicId = publicId }

    fun accessLevel(accessLevel: AccessLevel) = apply { this.accessLevel = accessLevel }

    fun workspaceId(workspaceId: WorkspaceId) = apply { this.workspaceId = workspaceId }

    fun encodedValue(encodedValue: String) = apply { this.encodedValue = encodedValue }

    fun build() = AccessToken(this)
  }

  val id: AccessTokenId

  val name: String

  val publicId: AccessTokenPublicId

  val accessLevel: AccessLevel

  val workspaceId: WorkspaceId

  val encodedValue: String

  init {
    requireNotNull(builder.id)
    requireNotNull(builder.name)
    requireNotNull(builder.publicId)
    requireNotNull(builder.accessLevel)
    requireNotNull(builder.workspaceId)
    requireNotNull(builder.encodedValue)
    id = builder.id!!
    name = builder.name!!
    publicId = builder.publicId!!
    accessLevel = builder.accessLevel!!
    workspaceId = builder.workspaceId!!
    encodedValue = builder.encodedValue!!
  }
}

enum class AccessLevel(val value: String) {
  READ_ONLY("read"),
  READ_WRITE("read-write"),
  ;

  companion object {
    fun from(value: String) = entries.first { it.value == value }
  }
}
