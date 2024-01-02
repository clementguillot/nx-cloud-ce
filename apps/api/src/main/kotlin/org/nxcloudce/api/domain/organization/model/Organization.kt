package org.nxcloudce.api.domain.organization.model

@JvmInline value class OrganizationId(val value: String)

data class Organization(val id: OrganizationId, val name: String)
