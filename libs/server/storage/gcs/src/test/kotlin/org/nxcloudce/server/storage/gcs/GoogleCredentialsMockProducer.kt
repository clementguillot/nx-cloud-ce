package org.nxcloudce.server.storage.gcs

import com.google.auth.Credentials
import com.google.auth.oauth2.ServiceAccountCredentials
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.Mock
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.enterprise.inject.Produces
import jakarta.inject.Singleton

@Mock
@ApplicationScoped
class GoogleCredentialsMockProducer {
  @Produces
  @Singleton
  @Default
  fun googleCredential(): Credentials {
    val serviceAccountCredentials = mockk<ServiceAccountCredentials>(relaxed = true)
    every { serviceAccountCredentials.account } returns "Account"
    every { serviceAccountCredentials.sign(any()) } returns byteArrayOf(123456.toByte())
    every { serviceAccountCredentials.universeDomain } returns "googleapis.com"

    return serviceAccountCredentials
  }
}
