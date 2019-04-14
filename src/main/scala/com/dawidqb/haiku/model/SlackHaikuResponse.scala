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

  private val supportedLanguages = Language.values.map(_.entryName)

  val DeleteOriginal = SlackHaikuResponse("", delete_original = true)
  val MissingLanguage = SlackHaikuResponse(
    s"""|*Missing language parameter*
        |*Usage*: /haiku [language code]    (Supported languages: ${supportedLanguages.mkString(", ")})""".stripMargin)
  val InvalidLanguage = SlackHaikuResponse(
    s"Unrecognized or unsupported language. Accepted values: ${supportedLanguages.mkString(", ")}")
}


