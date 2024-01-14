package org.nxcloudce.api.persistence.gateway

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.workspace.usecase.CreateWorkspaceRequest
import org.nxcloudce.api.persistence.entity.WorkspaceEntity
import org.nxcloudce.api.persistence.repository.WorkspacePanacheRepository
import kotlin.test.Test
import kotlin.test.assertEquals

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
      assertEquals(
        dummyEntityId.toString(),
        result.id.value,
      )
      assertEquals("test", result.name)
    }
}
