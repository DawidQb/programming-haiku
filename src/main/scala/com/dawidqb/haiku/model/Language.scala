package com.dawidqb.haiku.model

import enumeratum.EnumEntry.Lowercase
import enumeratum._

sealed trait Language extends EnumEntry

object Language extends Enum[Language] with CirceEnum[Language] with Lowercase {

  val values = findValues

  case object PL extends Language
  case object EN extends Language

}
