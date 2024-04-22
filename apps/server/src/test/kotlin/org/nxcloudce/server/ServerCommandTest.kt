package org.nxcloudce.server

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import com.github.ajalt.clikt.testing.test
import io.mockk.*
import io.quarkus.runtime.Quarkus
import org.junit.jupiter.api.Test
import org.nxcloudce.server.presentation.command.CleanupCommand

class ServerCommandTest {
  @Test
  fun `should start web server when using empty command`() {
    val command = Server()

    mockkStatic(Quarkus::class)
    every { Quarkus.run() } just runs

    val result = command.test()

    expect(result.statusCode).toEqual(0)
    verify(exactly = 1) { Quarkus.run() }
  }

  @Test
  fun `should start web server when using 'web' command`() {
    val command = Web()

    mockkStatic(Quarkus::class)
    every { Quarkus.run() } just runs

    val result = command.test()

    expect(result.statusCode).toEqual(0)
    verify(exactly = 1) { Quarkus.run() }
  }

  @Test
  fun `should start clean-up when using 'cleanup' command`() {
    val command = Cleanup()

    mockkStatic(Quarkus::class)
    every { Quarkus.run(CleanupCommand::class.java, "30") } just runs

    val result = command.test()

    expect(result.statusCode).toEqual(0)
    verify(exactly = 1) { Quarkus.run(CleanupCommand::class.java, "30") }
  }
}
