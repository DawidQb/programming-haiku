package com.dawidqb.haiku.model

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._

final case class SlackHaikuResponse(
                                     text: String,
                                     attachments: List[SlackAttachment] = Nil,
                                     delete_original: Boolean = false,
                                     response_type: String = "ephemeral"
                                   )

object SlackHaikuResponse {

  implicit val encoder: EntityEncoder[IO, SlackHaikuResponse] = jsonEncoderOf[IO, SlackHaikuResponse]

  val DeleteOriginal = SlackHaikuResponse("", delete_original = true)
  val MissingLanguage = SlackHaikuResponse(
    """|*Missing language parameter*
       |*Usage*: /haiku [language code]
       |(Supported languages: en, pl)""".stripMargin)
  val InvalidLanguage = SlackHaikuResponse("Unrecognized or unsupported language. Accepted values: en, pl")
}


