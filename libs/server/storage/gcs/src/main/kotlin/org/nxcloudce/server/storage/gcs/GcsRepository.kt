package org.nxcloudce.server.storage.gcs

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.nxcloudce.server.storage.core.FileRepository
import java.util.concurrent.TimeUnit

class GcsRepository private constructor(
  private val storage: Storage,
  private val bucket: String,
  private val dispatcher: CoroutineDispatcher,
) : FileRepository {
  companion object {
    private val presignExpirationDuration = 1L
    private val presignExpirationTimeUnit = TimeUnit.HOURS

    operator fun invoke(block: Builder.() -> Unit): GcsRepository {
      val builder = Builder()
      block(builder)
      return builder.build()
    }
  }

  override suspend fun generateGetUrl(objectPath: String): String =
    coroutineScope {
      withContext(dispatcher) {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectPath)).build()
        val url =
          storage.signUrl(
            blobInfo,
            presignExpirationDuration,
            presignExpirationTimeUnit,
            Storage.SignUrlOption.httpMethod(HttpMethod.GET),
            Storage.SignUrlOption.withV4Signature(),
          )
        url.toString()
      }
    }

  override suspend fun generatePutUrl(objectPath: String): String =
    coroutineScope {
      withContext(dispatcher) {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectPath)).build()
        val url =
          storage.signUrl(
            blobInfo,
            presignExpirationDuration,
            presignExpirationTimeUnit,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature(),
          )
        url.toString()
      }
    }

  override suspend fun deleteFile(objectPath: String) {
    coroutineScope {
      withContext(dispatcher) {
        storage.delete(BlobId.of(bucket, objectPath))
      }
    }
  }

  class Builder {
    var gcsConfiguration: GcsConfiguration? = null
    var storage: Storage? = null
    var bucket: String? = null
    var dispatcher: CoroutineDispatcher? = null

    fun build(): GcsRepository {
      requireNotNull(dispatcher)
      return gcsConfiguration?.let {
        GcsRepository(
          it.buildStorage(),
          it.bucket,
          dispatcher!!,
        )
      } ?: run {
        requireNotNull(storage)
        requireNotNull(bucket)
        GcsRepository(storage!!, bucket!!, dispatcher!!)
      }
    }
  }
}
