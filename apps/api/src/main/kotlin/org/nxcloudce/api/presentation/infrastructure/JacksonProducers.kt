package org.nxcloudce.api.presentation.infrastructure

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
      .registerModule(JavaTimeModule())
}
