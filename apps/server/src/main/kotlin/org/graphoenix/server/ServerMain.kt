package org.graphoenix.server

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class ServerMain {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) = Server().subcommands(Web(), Cleanup()).main(args)
  }
}
