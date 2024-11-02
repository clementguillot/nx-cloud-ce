package org.nxcloudce.server.persistence

import ProjectCodec
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry
import org.nxcloudce.server.persistence.codec.ProjectConfigurationCodec
import org.nxcloudce.server.persistence.codec.ProjectGraphCodec
import org.nxcloudce.server.persistence.codec.ProjectMetadataCodec
import org.nxcloudce.server.persistence.entity.RunEntity

@Suppress("unused")
class PersistenceCodecProvider : CodecProvider {
  @Suppress("UNCHECKED_CAST")
  override fun <T : Any> get(
    clazz: Class<T>,
    registry: CodecRegistry,
  ): Codec<T>? =
    when (clazz) {
      RunEntity.ProjectGraph::class.java -> ProjectGraphCodec(registry) as Codec<T>
      RunEntity.ProjectGraph.Project::class.java -> ProjectCodec(registry) as Codec<T>
      RunEntity.ProjectGraph.Project.ProjectConfiguration::class.java -> ProjectConfigurationCodec(registry) as Codec<T>
      RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata::class.java -> ProjectMetadataCodec() as Codec<T>
      else -> null
    }
}
