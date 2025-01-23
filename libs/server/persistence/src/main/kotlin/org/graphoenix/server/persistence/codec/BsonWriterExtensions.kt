package org.graphoenix.server.persistence.codec

import org.bson.BsonWriter

inline fun BsonWriter.writeRequiredField(
  fieldName: String,
  writer: BsonWriter.() -> Unit,
) {
  writeName(fieldName)
  writer()
}

fun <T> BsonWriter.writeNullableField(
  fieldName: String,
  value: T?,
  writer: BsonWriter.(T) -> Unit,
) {
  writeName(fieldName)
  value?.let { writer(it) } ?: writeNull()
}

inline fun BsonWriter.writeArrayField(writer: BsonWriter.() -> Unit) {
  writeStartArray()
  writer()
  writeEndArray()
}

inline fun <T> BsonWriter.writeDocumentMap(
  map: Map<String, T>,
  crossinline valueWriter: BsonWriter.(T) -> Unit,
) {
  writeStartDocument()
  map.forEach { (key, value) ->
    writeName(key)
    valueWriter(value)
  }
  writeEndDocument()
}
