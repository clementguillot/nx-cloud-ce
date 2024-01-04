package org.nxcloudce.api.domain.workspace.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AccessTokenTest {
  @Test
  fun `should build a new instance of AccessToken`() {
    val accessToken =
      AccessToken.Builder()
        .id(AccessTokenId("new-id"))
        .name("new name")
        .publicId(AccessTokenPublicId("public-id"))
        .accessLevel(AccessLevel.READ_ONLY)
        .workspaceId(WorkspaceId("workspace-id"))
        .encodedValue("base64content")
        .build()

    assertEquals("new-id", accessToken.id.value)
    assertEquals("new name", accessToken.name)
    assertEquals("public-id", accessToken.publicId.value)
    assertEquals(AccessLevel.READ_ONLY, accessToken.accessLevel)
    assertEquals("workspace-id", accessToken.workspaceId.value)
    assertEquals("base64content", accessToken.encodedValue)
  }
}
