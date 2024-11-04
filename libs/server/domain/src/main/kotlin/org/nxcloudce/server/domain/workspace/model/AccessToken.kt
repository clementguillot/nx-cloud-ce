package org.nxcloudce.server.domain.workspace.model

import java.util.*

@JvmInline value class AccessTokenId(
  val value: String,
)

@JvmInline value class AccessTokenPublicId(
  val value: String = UUID.randomUUID().toString(),
)

class AccessToken private constructor(
  builder: Builder,
) {
  companion object {
    operator fun invoke(block: Builder.() -> Unit): AccessToken {
      val builder = Builder()
      block(builder)
      return builder.build()
    }
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

  class Builder {
    var id: AccessTokenId? = null
    var name: String? = null
    var publicId: AccessTokenPublicId? = null
    var accessLevel: AccessLevel? = null
    var workspaceId: WorkspaceId? = null
    var encodedValue: String? = null

    fun build() = AccessToken(this)
  }
}

enum class AccessLevel(
  val value: String,
) {
  READ_ONLY("read"),
  READ_WRITE("read-write"),
  ;

  companion object {
    fun from(value: String): AccessLevel = entries.find { it.value == value } ?: throw IllegalArgumentException()
  }
}
