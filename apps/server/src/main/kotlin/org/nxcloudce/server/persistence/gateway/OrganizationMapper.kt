package org.nxcloudce.server.persistence.gateway

import org.nxcloudce.server.domain.organization.model.Organization
import org.nxcloudce.server.domain.organization.model.OrganizationId
import org.nxcloudce.server.domain.organization.usecase.CreateOrganizationRequest
import org.nxcloudce.server.persistence.entity.OrganizationEntity

fun OrganizationEntity.toDomain() = Organization(id = OrganizationId(id.toString()), name = name)

fun CreateOrganizationRequest.toEntity() = OrganizationEntity(id = null, name = name)
