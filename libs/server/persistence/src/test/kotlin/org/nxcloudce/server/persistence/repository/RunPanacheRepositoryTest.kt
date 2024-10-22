package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.RunEntity
import java.time.LocalDateTime

@QuarkusTest
class RunPanacheRepositoryTest {
  @Inject
  lateinit var runPanacheRepository: RunPanacheRepository

  @BeforeEach
  fun setUp() {
    runBlocking {
      runPanacheRepository.deleteAll().awaitSuspending()
    }
  }

  @Test
  fun `should persist new entity`() =
    runTest {
      val runEntity = buildRunEntity()
      runPanacheRepository.persist(runEntity).awaitSuspending()

      val count = runPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }

  @Test
  fun `should find entities with 'endTime' older than input`() =
    runTest {
      val olderDate = LocalDateTime.now().minusDays(5)
      runPanacheRepository.persist(listOf(buildRunEntity())).awaitSuspending()
      runPanacheRepository.persist(listOf(buildRunEntity(olderDate))).awaitSuspending()

      val result = runPanacheRepository.findAllByEndTimeLowerThan(LocalDateTime.now().minusDays(1)).asFlow().toList()
      val totalCount = runPanacheRepository.count().awaitSuspending()

      expect(result.size).toEqual(1)
      expect(result[0].endTime < LocalDateTime.now().minusDays(1)).toEqual(true)
      expect(totalCount).toEqual(2)
    }

  fun buildRunEntity(endTime: LocalDateTime = LocalDateTime.now()): RunEntity =
    RunEntity(
      id = null,
      workspaceId = ObjectId(),
      command = "test command",
      status = "test status",
      startTime = LocalDateTime.now(),
      endTime = endTime,
      branch = "test branch",
      runGroup = "test run group",
      inner = true,
      distributedExecutionId = "test distributed execution id",
      ciExecutionId = "test ci execution id",
      ciExecutionEnv = "test ci execution env",
      machineInfo = RunEntity.MachineInfo(),
      meta = mapOf("key" to "value"),
      vcsContext =
        RunEntity.VcsContext(
          branch = "test branch",
          ref = "test ref",
          title = "test title",
          headSha = "test head sha",
          baseSha = "test base sha",
          commitLink = "test commit link",
          author = "test author",
          authorUrl = "test author url",
          authorAvatarUrl = "test author avatar url",
          repositoryUrl = "test repository url",
          platformName = "test platform name",
        ),
      linkId = "test link id",
      projectGraph =
        RunEntity.ProjectGraph(
          nodes =
            mapOf(
              "node" to
                RunEntity.ProjectGraph.Project(
                  type = "test type",
                  name = "test name",
                  data =
                    RunEntity.ProjectGraph.Project.Data(
                      root = "root",
                      sourceRoot = "root",
                      // metadata = emptyMap(),
                      // targets = emptyMap(),
                    ),
                ),
            ),
          dependencies =
            mapOf(
              "dependency" to
                listOf(
                  RunEntity.ProjectGraph.Dependency(
                    source = "test source",
                    target = "test target",
                    type = "test type",
                  ),
                ),
            ),
        ),
      hashedContributors = listOf("test hashed contributors"),
      sha = "test sha",
    )
}
