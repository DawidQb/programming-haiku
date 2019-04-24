package com.dawidkubicki.haiku.model

import io.circe.{Decoder, Encoder}

final case class UserId(value: String) extends AnyVal

object UserId {

  implicit val decoder: Decoder[UserId] = Decoder.decodeString.map(UserId(_))
  implicit val encoder: Encoder[UserId] = Encoder.encodeString.contramap(_.value)

}
