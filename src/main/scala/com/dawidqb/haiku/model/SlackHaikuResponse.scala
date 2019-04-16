package com.dawidqb.haiku.model

import cats.effect.IO
import com.dawidqb.haiku.model.ResponseType.Ephemeral
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.EntityEncoder
import org.http4s.circe._

final case class SlackHaikuResponse(
                                     text: String,
                                     attachments: List[SlackAttachment] = Nil,
                                     delete_original: Boolean = false,
                                     response_type: ResponseType = Ephemeral
                                   )

object SlackHaikuResponse {

  implicit val decoder: Decoder[SlackHaikuResponse] = deriveDecoder[SlackHaikuResponse]
  implicit val encoder: Encoder[SlackHaikuResponse] = deriveEncoder[SlackHaikuResponse]

  implicit val entityEncoder: EntityEncoder[IO, SlackHaikuResponse] = jsonEncoderOf[IO, SlackHaikuResponse]

  private val supportedLanguages = Language.values.map(_.entryName)

  val DeleteOriginal = SlackHaikuResponse("", delete_original = true)
  val MissingLanguage = SlackHaikuResponse(
    s"""|*Missing language parameter*
        |*Usage*: /haiku [language code]    (Supported languages: ${supportedLanguages.mkString(", ")})""".stripMargin)
  val InvalidLanguage = SlackHaikuResponse(
    s"Unrecognized or unsupported language. Accepted values: ${supportedLanguages.mkString(", ")}")
}


