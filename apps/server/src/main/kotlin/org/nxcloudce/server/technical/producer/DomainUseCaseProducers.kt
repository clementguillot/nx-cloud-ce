package org.nxcloudce.server.technical.producer

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.nxcloudce.server.domain.metric.interactor.SaveMetricsImpl
import org.nxcloudce.server.domain.metric.usecase.SaveMetrics
import org.nxcloudce.server.domain.organization.interactor.CreateOrganizationImpl
import org.nxcloudce.server.domain.organization.usecase.CreateOrganization
import org.nxcloudce.server.domain.run.interactor.CleanupRunImpl
import org.nxcloudce.server.domain.run.interactor.EndRunImpl
import org.nxcloudce.server.domain.run.interactor.StartRunImpl
import org.nxcloudce.server.domain.run.usecase.CleanupRun
import org.nxcloudce.server.domain.run.usecase.EndRun
import org.nxcloudce.server.domain.run.usecase.StartRun
import org.nxcloudce.server.domain.workspace.interactor.CreateOrgAndWorkspaceImpl
import org.nxcloudce.server.domain.workspace.interactor.CreateWorkspaceImpl
import org.nxcloudce.server.domain.workspace.interactor.GetWorkspaceAccessTokenImpl
import org.nxcloudce.server.domain.workspace.usecase.CreateOrgAndWorkspace
import org.nxcloudce.server.domain.workspace.usecase.CreateWorkspace
import org.nxcloudce.server.domain.workspace.usecase.GetWorkspaceAccessToken
import org.nxcloudce.server.persistence.gateway.*
import org.nxcloudce.server.storage.gateway.StorageServiceImpl

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
