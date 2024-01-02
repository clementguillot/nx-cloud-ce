package org.nxcloudce.api.persistence.gateway

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.gateway.WorkspaceRepository
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.persistence.entity.WorkspaceEntity
import org.nxcloudce.api.persistence.repository.WorkspacePanacheRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@QuarkusTest
class WorkspaceImplWorkspaceTest {
  @InjectMock
  lateinit var mockWorkspacePanacheRepository: WorkspacePanacheRepository

  @Inject
  lateinit var workspaceRepository: WorkspaceRepository

  @Test
  fun `should create a new workspace in the DB`() =
    runTest {
      // Given
      val dummyEntityId = ObjectId()
      val dummyEntityWithId = WorkspaceEntity(dummyEntityId, ObjectId(), "test")
      val dummyRequest = CreateWorkspaceRequest(OrganizationId(dummyEntityWithId.orgId.toString()), "test")

      every { mockWorkspacePanacheRepository.persist(any<WorkspaceEntity>()) } answers {
        (firstArg<WorkspaceEntity>()).id = dummyEntityId
        Uni.createFrom().item(dummyEntityWithId)
      }

      // When
      val result = workspaceRepository.create(dummyRequest)

      // Then
      assertEquals(
        result.id.value,
        dummyEntityId.toString(),
      )
      assertEquals(result.name, dummyEntityWithId.name)
    }
}
