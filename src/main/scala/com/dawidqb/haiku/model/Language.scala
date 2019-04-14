package com.dawidqb.haiku.model

import enumeratum.EnumEntry.Lowercase
import enumeratum._

sealed trait Language extends EnumEntry

object Language extends Enum[Language] with Lowercase { // FIXME Lowercase doesn't seem to work

  val values = findValues

  case object pl extends Language
  case object en extends Language

}
