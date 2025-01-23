package org.graphoenix.server.gateway.persistence

import org.graphoenix.server.domain.organization.model.Organization
import org.graphoenix.server.domain.organization.model.OrganizationId
import org.graphoenix.server.domain.organization.usecase.CreateOrganizationRequest
import org.graphoenix.server.persistence.entity.OrganizationEntity

fun OrganizationEntity.toDomain() = Organization(id = OrganizationId(id.toString()), name = name)

fun CreateOrganizationRequest.toEntity() = OrganizationEntity(id = null, name = name)
