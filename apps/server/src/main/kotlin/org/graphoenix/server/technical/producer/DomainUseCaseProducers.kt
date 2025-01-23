package org.graphoenix.server.technical.producer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.graphoenix.server.domain.metric.interactor.SaveMetricsImpl
import org.graphoenix.server.domain.metric.usecase.SaveMetrics
import org.graphoenix.server.domain.organization.interactor.CreateOrganizationImpl
import org.graphoenix.server.domain.organization.usecase.CreateOrganization
import org.graphoenix.server.domain.run.interactor.CleanupRunImpl
import org.graphoenix.server.domain.run.interactor.EndRunImpl
import org.graphoenix.server.domain.run.interactor.StartRunImpl
import org.graphoenix.server.domain.run.usecase.CleanupRun
import org.graphoenix.server.domain.run.usecase.EndRun
import org.graphoenix.server.domain.run.usecase.StartRun
import org.graphoenix.server.domain.workspace.interactor.CreateOrgAndWorkspaceImpl
import org.graphoenix.server.domain.workspace.interactor.CreateWorkspaceImpl
import org.graphoenix.server.domain.workspace.interactor.GetWorkspaceAccessTokenImpl
import org.graphoenix.server.domain.workspace.usecase.CreateOrgAndWorkspace
import org.graphoenix.server.domain.workspace.usecase.CreateWorkspace
import org.graphoenix.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.graphoenix.server.gateway.persistence.*
import org.graphoenix.server.gateway.storage.StorageServiceImpl

@ApplicationScoped
class DomainUseCaseProducers(
  private val accessTokenRepository: AccessTokenRepositoryImpl,
  private val artifactRepository: ArtifactRepositoryImpl,
  private val organizationRepository: OrganizationRepositoryImpl,
  private val runRepository: RunRepositoryImpl,
  private val taskRepository: TaskRepositoryImpl,
  private val taskRunnerMetricRepository: TaskRunnerMetricRepositoryImpl,
  private val workspaceRepository: WorkspaceRepositoryImpl,
  private val storageService: StorageServiceImpl,
) {
  @Produces
  @ApplicationScoped
  fun saveMetrics(): SaveMetrics = SaveMetricsImpl(taskRunnerMetricRepository)

  @Produces
  @ApplicationScoped
  fun createOrganization(): CreateOrganization = CreateOrganizationImpl(organizationRepository)

  @Produces
  @ApplicationScoped
  fun cleanupRun(): CleanupRun = CleanupRunImpl(runRepository, taskRepository, artifactRepository, storageService)

  @Produces
  @ApplicationScoped
  fun endRun(): EndRun = EndRunImpl(runRepository, taskRepository, artifactRepository)

  @Produces
  @ApplicationScoped
  fun startRun(): StartRun = StartRunImpl(artifactRepository, storageService)

  @Produces
  @ApplicationScoped
  fun createOrgAndWorkspace(): CreateOrgAndWorkspace =
    CreateOrgAndWorkspaceImpl(
      workspaceRepository,
      organizationRepository,
      accessTokenRepository,
    )

  @Produces
  @ApplicationScoped
  fun createWorkspace(): CreateWorkspace = CreateWorkspaceImpl(workspaceRepository, organizationRepository)

  @Produces
  @ApplicationScoped
  fun getWorkspaceAccessToken(): GetWorkspaceAccessToken = GetWorkspaceAccessTokenImpl(accessTokenRepository)
}
