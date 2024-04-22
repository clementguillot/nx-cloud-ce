package org.nxcloudce.server.presentation.command

import io.quarkus.runtime.QuarkusApplication
import kotlinx.coroutines.runBlocking
import org.jboss.logging.Logger
import org.nxcloudce.server.domain.run.usecase.CleanupRun
import org.nxcloudce.server.domain.run.usecase.CleanupRunRequest
import java.time.LocalDateTime

class CleanupCommand(private val cleanupRun: CleanupRun) : QuarkusApplication {
  companion object {
    private val logger = Logger.getLogger(CleanupCommand::class.java)
  }

  override fun run(vararg args: String): Int =
    runBlocking {
      val days = args[0].toInt()
      logger.info("Cleanup started! Removing runs that are older than $days days")
      cleanupRun(
        CleanupRunRequest(creationDateThreshold = LocalDateTime.now().minusDays(days.toLong())),
      ) {
        logger.info("Cleanup ended! # of deleted runs: ${it.deletedCount}")
      }
      0
    }
}
