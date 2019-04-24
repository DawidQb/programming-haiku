package com.dawidkubicki.haiku.model

import enumeratum.EnumEntry.Lowercase
import enumeratum._

sealed trait Language extends EnumEntry with Lowercase

object Language extends Enum[Language] with CirceEnum[Language] {

  val values = findValues

  case object EN extends Language
  case object PL extends Language

}
