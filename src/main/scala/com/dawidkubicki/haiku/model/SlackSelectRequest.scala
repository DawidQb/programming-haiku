package com.dawidkubicki.haiku.model

import cats.data.EitherT
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import org.http4s.{DecodeFailure, EntityDecoder, InvalidMessageBodyFailure, MalformedMessageBodyFailure, ParseFailure, UrlForm}
import com.dawidkubicki.haiku._

final case class SlackSelectRequest(
                                     callback_id: String,
                                     user: SelectionUser,
                                     actions: List[SelectionAction],
                                     response_url: String
                                   )

object SlackSelectRequest extends LazyLogging {

  implicit val decoder: Decoder[SlackSelectRequest] = deriveDecoder[SlackSelectRequest]
  implicit val encoder: Encoder[SlackSelectRequest] = deriveEncoder[SlackSelectRequest]

  implicit val entityDecoder: EntityDecoder[IO, SlackSelectRequest] =
    UrlForm.entityDecoder[IO].flatMapR { urlForm =>
      (for {
        str <- urlForm.values("payload").headOption
          .asEitherT(MalformedMessageBodyFailure("Missing payload field", None))
        parsed <- parse(str).asEitherT
          .leftMap(err => MalformedMessageBodyFailure("Malformed payload json", Some(err)))
        decoded <- parsed.as[SlackSelectRequest].asEitherT
          .leftMap(err => InvalidMessageBodyFailure("Failed decoding select request", Some(err)): DecodeFailure)
      } yield decoded).leftMap { err =>
        logger.error("Decoding SlackSelectRequest failed with error", err)
        err
      }
    }

}

final case class SelectionUser(id: UserId, name: String)

object SelectionUser {
  implicit val decoder: Decoder[SelectionUser] = deriveDecoder[SelectionUser]
  implicit val encoder: Encoder[SelectionUser] = deriveEncoder[SelectionUser]
}

final case class SelectionAction(name: String, `type`: String, value: ActionValue)

object SelectionAction {
  implicit val decoder: Decoder[SelectionAction] = deriveDecoder[SelectionAction]
  implicit val encoder: Encoder[SelectionAction] = deriveEncoder[SelectionAction]
}
