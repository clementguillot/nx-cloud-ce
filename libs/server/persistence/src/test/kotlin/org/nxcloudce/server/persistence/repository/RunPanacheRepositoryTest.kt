package org.nxcloudce.server.persistence.repository

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.nxcloudce.server.persistence.entity.RunEntity
import java.time.LocalDateTime

@QuarkusTest
class RunPanacheRepositoryTest {
  @Inject
  lateinit var runPanacheRepository: RunPanacheRepository

  @Test
  fun `should persist new entity`() =
    runTest {
      val runEntity =
        RunEntity(
          id = null,
          workspaceId = ObjectId(),
          command = "test command",
          status = "test status",
          startTime = LocalDateTime.now(),
          endTime = LocalDateTime.now(),
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
                    RunEntity.ProjectGraph.Node(
                      type = "test type",
                      name = "test name",
                      data = mapOf("key" to "value"),
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
          hashedContributors = "test hashed contributors",
          sha = "test sha",
        )
      runPanacheRepository.persist(runEntity).awaitSuspending()

      val count = runPanacheRepository.count().awaitSuspending()

      expect(count).toEqual(1)
    }
}
