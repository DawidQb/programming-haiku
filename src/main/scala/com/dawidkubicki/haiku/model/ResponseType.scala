package com.dawidkubicki.haiku.model

import enumeratum.EnumEntry.Snakecase
import enumeratum._

sealed trait ResponseType extends EnumEntry with Snakecase

object ResponseType extends Enum[ResponseType] with CirceEnum[ResponseType] {

  val values = findValues

  case object InChannel extends ResponseType
  case object Ephemeral extends ResponseType

}
