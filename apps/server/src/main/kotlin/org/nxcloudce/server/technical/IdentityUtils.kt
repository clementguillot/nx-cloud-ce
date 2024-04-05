package org.nxcloudce.server.technical

import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.nxcloudce.server.domain.workspace.model.WorkspaceId

suspend fun CurrentIdentityAssociation.getWorkspaceId(): WorkspaceId = WorkspaceId(deferredIdentity.awaitSuspending().principal.name)
