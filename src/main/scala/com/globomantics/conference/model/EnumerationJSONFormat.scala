package com.globomantics.conference.model

import spray.json.{JsString, JsValue, RootJsonFormat}

class EnumerationJSONFormat[A](enum: Enumeration) extends RootJsonFormat[A] {
  def write(obj: A): JsValue = JsString(obj.toString)

  def read(json: JsValue): A = json match {
    case JsString(str) => enum.withName(str).asInstanceOf[A]
    case x => throw new RuntimeException(s"unknown enumeration value: $x")
  }
}