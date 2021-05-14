package com.globomantics.conference.model

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

import java.util.UUID

class UUIDJsonFormat extends JsonFormat[UUID] {
  def write(uuid: UUID): JsString = JsString(uuid.toString)

  def read(value: JsValue): UUID = {
    value match {
      case JsString(uuid) => UUID.fromString(uuid)
      case _ => throw DeserializationException("Expected hexadecimal UUID string")
    }
  }
}