package org.graphoenix.server.persistence.codec

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.graphoenix.server.persistence.entity.RunEntity

class ProjectMetadataCodec : Codec<RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata> {
  override fun encode(
    writer: BsonWriter,
    value: RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata,
    encoderContext: EncoderContext,
  ) = writer.run {
    writeStartDocument()
    writeFields(value)
    writeEndDocument()
  }

  private fun BsonWriter.writeFields(value: RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata) {
    writeNullableField("description", value.description) { writeString(it) }
    writeNullableField("technologies", value.technologies) { technologies ->
      writeArrayField { technologies.forEach { writeString(it) } }
    }
    writeNullableField("targetGroups", value.targetGroups) { groups ->
      writeStartDocument()
      groups.forEach { (group, targets) ->
        writeName(group)
        writeArrayField { targets.forEach { writeString(it) } }
      }
      writeEndDocument()
    }
  }

  override fun decode(
    reader: BsonReader,
    decoderContext: DecoderContext,
  ): RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata =
    reader.run {
      readStartDocument()
      val metadata = readMetadataFields()
      readEndDocument()
      metadata
    }

  private fun BsonReader.readMetadataFields(): RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata {
    val description = readNullableField("description") { readString() }
    val technologies = readNullableField("technologies") { readStringArray() }
    val targetGroups = readNullableField("targetGroups") { readTargetGroups() }
    return RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata(
      description,
      technologies,
      targetGroups,
    )
  }

  private fun BsonReader.readTargetGroups(): Map<String, Collection<String>> =
    buildMap {
      readStartDocument()
      while (readBsonType() != BsonType.END_OF_DOCUMENT) {
        val groupName = readName()
        put(groupName, readStringArray())
      }
      readEndDocument()
    }

  override fun getEncoderClass(): Class<RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata> =
    RunEntity.ProjectGraph.Project.ProjectConfiguration.Metadata::class.java
}
