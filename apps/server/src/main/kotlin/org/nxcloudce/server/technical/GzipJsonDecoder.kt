package org.nxcloudce.server.technical

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jboss.logging.Logger
import java.util.zip.GZIPInputStream
import kotlin.reflect.KClass
import kotlin.text.Charsets.UTF_8

@ApplicationScoped
class GzipJsonDecoder(
  private val dispatcher: CoroutineDispatcher,
  private val objectMapper: ObjectMapper,
) {
  companion object {
    private val logger = Logger.getLogger(GzipJsonDecoder::class.java)
  }

  suspend fun <T : Any> from(
    buffer: ByteArray,
    type: KClass<T>,
  ): T =
    coroutineScope {
      withContext(dispatcher) {
        buffer.inputStream().use { byteStream ->
          GZIPInputStream(byteStream).use { gzipStream ->
            gzipStream.bufferedReader(UTF_8).readText().let { text ->
              jsonTextToObject(text, type)
            }
          }
        }
      }
    }

  private fun <T : Any> jsonTextToObject(
    text: String,
    type: KClass<T>,
  ): T {
    try {
      return objectMapper.readValue(text, type.java)
    } catch (e: Exception) {
      logger.error("Error has occurred while processing JSON: $text", e)
      throw e
    }
  }
}
