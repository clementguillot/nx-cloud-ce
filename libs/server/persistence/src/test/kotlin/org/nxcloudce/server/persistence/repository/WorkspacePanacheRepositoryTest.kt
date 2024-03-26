package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.WorkspaceEntity
import java.time.LocalDateTime

@QuarkusTest
class WorkspacePanacheRepositoryTest {
  @Inject
  lateinit var workspacePanacheRepository: WorkspacePanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val workspaceEntity =
        WorkspaceEntity(
          id = ObjectId(),
          orgId = ObjectId(),
          name = "Test Workspace",
          installationSource = "Test Source",
          nxInitDate = LocalDateTime.now(),
        )
      workspacePanacheRepository.persist(workspaceEntity).awaitSuspending()

      val count = workspacePanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
