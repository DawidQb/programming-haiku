package com.dawidqb.haiku.model

import enumeratum.EnumEntry.Lowercase
import enumeratum._

sealed trait ActionValue extends EnumEntry with Lowercase

object ActionValue extends Enum[ActionValue] with CirceEnum[ActionValue]  {

  val values = findValues

  case object Cancel extends ActionValue
  case object Send extends ActionValue
  case object Shuffle extends ActionValue

}

