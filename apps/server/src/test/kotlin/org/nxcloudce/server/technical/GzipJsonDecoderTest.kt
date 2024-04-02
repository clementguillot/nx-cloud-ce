package org.nxcloudce.server.technical

import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

@QuarkusTest
class GzipJsonDecoderTest {
  @Inject
  lateinit var gzipDecoder: GzipJsonDecoder

  @Test
  fun `Test GzipDecoder when input is valid`() =
    runTest {
      val dummyObject = DummyClass("test value", 42)

      val outputStream = ByteArrayOutputStream()
      GZIPOutputStream(outputStream).bufferedWriter().use {
        it.write(
          """
          {
            "stringProperty": "test value",
            "intProperty":42
          }
          """.trimIndent(),
        )
      }

      val result = gzipDecoder.from(outputStream.toByteArray(), DummyClass::class)
      expect(result).toEqual(dummyObject)
    }

  @Test
  fun `Test GzipDecoder when input is invalid`() =
    runTest {
      val outputStream = ByteArrayOutputStream()
      GZIPOutputStream(outputStream).bufferedWriter().use {
        it.write(
          """
          {
            "invalid": "property"
          }
          """.trimIndent(),
        )
      }

      expect {
        runBlocking {
          gzipDecoder.from(outputStream.toByteArray(), DummyClass::class)
        }
      }.toThrow<Exception>()
    }

  data class DummyClass(val stringProperty: String, val intProperty: Int)
}
