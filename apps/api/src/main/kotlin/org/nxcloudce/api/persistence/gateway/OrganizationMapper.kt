package org.nxcloudce.api.persistence.gateway

import org.nxcloudce.api.domain.organization.model.Organization
import org.nxcloudce.api.domain.organization.model.OrganizationId
import org.nxcloudce.api.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.api.persistence.entity.OrganizationEntity

fun OrganizationEntity.toDomain(): Organization = Organization(id = OrganizationId(id.toString()), name = name)

fun CreateOrganizationRequest.toEntity(): OrganizationEntity = OrganizationEntity(id = null, name = name)
