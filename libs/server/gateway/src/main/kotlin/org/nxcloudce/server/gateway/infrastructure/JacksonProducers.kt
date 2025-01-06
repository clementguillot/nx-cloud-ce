package org.nxcloudce.server.gateway.infrastructure

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class JacksonProducers {
  @Produces
  fun objectMapper(): ObjectMapper =
    jacksonObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(JavaTimeModule())
}
