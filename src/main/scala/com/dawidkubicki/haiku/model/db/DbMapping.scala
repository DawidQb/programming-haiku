package com.dawidkubicki.haiku.model.db

import java.util.UUID

import doobie.util.{Get, Put}
import enumeratum.{Enum, EnumEntry}

import scala.reflect.ClassTag

trait DbMapping {
  implicit val uuidGet: Get[UUID] = Get[String].map(UUID.fromString)
  implicit val uuidPut: Put[UUID] = Put[String].contramap(_.toString)

  implicit def enumGet[E <: EnumEntry : Enum : ClassTag]: Get[E] = Get[String].map(implicitly[Enum[E]].withName)
  implicit def enumPut[E <: EnumEntry]: Put[E] = Put[String].contramap(_.entryName)

  implicit val haikuIdGet: Get[HaikuId] = Get[UUID].map(HaikuId.apply)
  implicit val haikuIdPut: Put[HaikuId] = Put[UUID].contramap(_.value)
}
