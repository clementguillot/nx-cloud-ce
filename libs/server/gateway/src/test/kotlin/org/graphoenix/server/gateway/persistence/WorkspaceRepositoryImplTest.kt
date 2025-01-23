package org.graphoenix.server.gateway.persistence

import ch.tutteli.atrium.api.fluent.en_GB.its
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspaceRequest
import org.graphoenix.server.persistence.entity.WorkspaceEntity
import org.graphoenix.server.persistence.repository.WorkspacePanacheRepository
import org.junit.jupiter.api.Test

@QuarkusTest
class WorkspaceRepositoryImplTest {
  @InjectMock
  lateinit var mockWorkspacePanacheRepository: WorkspacePanacheRepository

  @Inject
  lateinit var workspaceRepository: WorkspaceRepositoryImpl

  @Test
  fun `should create a new workspace in the DB`() =
    runTest {
      // Given
      val dummyEntityId = ObjectId()
      val dummyRequest = CreateWorkspaceRequest(OrganizationId(ObjectId().toString()), "test")

      every { mockWorkspacePanacheRepository.persist(any<WorkspaceEntity>()) } answers
        {
          (firstArg<WorkspaceEntity>()).id = dummyEntityId
          Uni.createFrom().item(firstArg<WorkspaceEntity>())
        }

      // When
      val result = workspaceRepository.create(dummyRequest)

      // Then
      expect(result) {
        its { id.value }.toEqual(dummyEntityId.toString())
        its { name }.toEqual("test")
      }
    }
}
