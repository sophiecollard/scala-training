package com.jpmc.json

sealed trait Json

object Json {

  case object Null extends Json
  final case class Boolean(value: scala.Boolean) extends Json
  final case class Int(value: scala.Int) extends Json
  final case class String(value: java.lang.String) extends Json
  sealed trait Document extends Json
  final case class Array(value: List[Json]) extends Document
  final case class Object(value: Map[java.lang.String, Json]) extends Document

  def `null`: Json = Null
  def apply(value: scala.Boolean): Json = Boolean(value)
  def apply(value: scala.Int): Json = Int(value)
  def apply(value: java.lang.String): Json = String(value)
  def apply(value: List[Json]): Json = Array(value)
  def apply(value: Map[java.lang.String, Json]): Json = Object(value)

  def printJson(json: Json): java.lang.String = json match {
    case Null           => "null"
    case Boolean(value) => s"$value"
    case Int(value)     => s"$value"
    case String(value)  => s""""$value""""
    case doc: Document  => printDocument(doc)
  }

  def printDocument(doc: Document): java.lang.String = doc match {
    case Array(value)  => s"[${value.map(printJson).mkString(",")}]"
    case Object(value) => s"{${value.map { case (k, v) => s"$k:${printJson(v)}" }}"
  }

  def dropNulls(json: Json): Option[Json] = json match {
    case Null          => None
    case b: Boolean    => Some(b)
    case i: Int        => Some(i)
    case s: String     => Some(s)
    case Array(value)  => Some(Array(value.flatMap(dropNulls)))
    case Object(value) => Some(Object(value.flatMap { case (k, v) => dropNulls(v).map((k, _)) }))
  }

}
