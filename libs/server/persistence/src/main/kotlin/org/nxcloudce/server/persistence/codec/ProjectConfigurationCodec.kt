package org.nxcloudce.server.persistence.codec

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import org.nxcloudce.server.persistence.entity.RunEntity

class ProjectConfigurationCodec(
  private val registry: CodecRegistry,
) : Codec<RunEntity.ProjectGraph.Project.ProjectConfiguration> {
  private val metadataCodec by lazy {
    registry[RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata::class.java]
  }

  override fun encode(
    writer: BsonWriter,
    value: RunEntity.ProjectGraph.Project.ProjectConfiguration,
    encoderContext: EncoderContext,
  ) = writer.run {
    writeStartDocument()
    writeFields(value, encoderContext)
    writeEndDocument()
  }

  private fun BsonWriter.writeFields(
    value: RunEntity.ProjectGraph.Project.ProjectConfiguration,
    encoderContext: EncoderContext,
  ) {
    writeString("root", value.root)
    writeNullableField("sourceRoot", value.sourceRoot) { writeString(it) }
    writeNullableField("targets", value.targets) { targets ->
      writeStartDocument()
      targets.forEach { (group, target) -> writeString(group, target) }
      writeEndDocument()
    }
    writeNullableField("metadata", value.metadata) { metadata ->
      metadataCodec.encode(this, metadata, encoderContext)
    }
  }

  override fun decode(
    reader: BsonReader,
    decoderContext: DecoderContext?,
  ): RunEntity.ProjectGraph.Project.ProjectConfiguration =
    reader.run {
      readStartDocument()
      val configuration = readConfigurationFields(decoderContext)
      readEndDocument()
      configuration
    }

  private fun BsonReader.readConfigurationFields(decoderContext: DecoderContext?): RunEntity.ProjectGraph.Project.ProjectConfiguration {
    val root = readRequiredField("root") { readString() }
    val sourceRoot = readNullableField("sourceRoot") { readString() }
    val targets = readNullableField("targets") { readTargetMappings() }
    val metadata =
      readNullableField("metadata") {
        metadataCodec.decode(this, decoderContext)
      }

    return RunEntity.ProjectGraph.Project.ProjectConfiguration(
      root = root,
      sourceRoot = sourceRoot,
      targets = targets,
      metadata = metadata,
    )
  }

  private fun BsonReader.readTargetMappings(): Map<String, String> =
    buildMap {
      readStartDocument()
      while (readBsonType() != BsonType.END_OF_DOCUMENT) {
        put(readName(), readString())
      }
      readEndDocument()
    }

  override fun getEncoderClass(): Class<RunEntity.ProjectGraph.Project.ProjectConfiguration> =
    RunEntity.ProjectGraph.Project.ProjectConfiguration::class.java
}
