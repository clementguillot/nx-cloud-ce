package org.nxcloudce.api.presentation.dto

sealed class VerifyClientBundleDto(
  val valid: Boolean,
  val url: String?,
  val version: String?,
) {
  class ValidVerifyClientBundleDto :
    VerifyClientBundleDto(true, null, null)

  class InvalidVerifyClientBundleDto(url: String, version: String) :
    VerifyClientBundleDto(false, url, version)
}
