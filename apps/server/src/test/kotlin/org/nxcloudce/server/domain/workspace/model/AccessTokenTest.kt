package org.nxcloudce.server.domain.workspace.model

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import org.junit.jupiter.api.Test

class AccessTokenTest {
  @Test
  fun `should build a new instance of AccessToken`() {
    val accessToken =
      AccessToken {
        id = AccessTokenId("new-id")
        name = "new name"
        publicId = AccessTokenPublicId("public-id")
        accessLevel = AccessLevel.READ_ONLY
        workspaceId = WorkspaceId("workspace-id")
        encodedValue = "base64content"
      }

    expect(accessToken) {
      its { id.value }.toEqual("new-id")
      its { name }.toEqual("new name")
      its { publicId.value }.toEqual("public-id")
      its { accessLevel }.toEqual(AccessLevel.READ_ONLY)
      its { workspaceId.value }.toEqual("workspace-id")
      its { encodedValue }.toEqual("base64content")
    }
  }
}
