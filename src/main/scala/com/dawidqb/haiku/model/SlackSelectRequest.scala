package com.dawidqb.haiku.model

import cats.effect.IO
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackSelectRequest(
                                     callback_id: String,
                                     user: SelectionUser,
                                     actions: List[SelectionAction],
                                     response_url: String
                                   )

object SlackSelectRequest {

  implicit val decoder: Decoder[SlackSelectRequest] = deriveDecoder[SlackSelectRequest]
  implicit val eEncoder: Encoder[SlackSelectRequest] = deriveEncoder[SlackSelectRequest]

  implicit val entityDecoder: EntityDecoder[IO, SlackSelectRequest] =
    UrlForm.entityDecoder[IO].map { urlForm =>
      parse(urlForm.values("payload").head).right.get.as[SlackSelectRequest].right.get
    }

}

final case class SelectionUser(id: UserId, name: String)

object SelectionUser {
  implicit val decoder: Decoder[SelectionUser] = deriveDecoder[SelectionUser]
  implicit val eEncoder: Encoder[SelectionUser] = deriveEncoder[SelectionUser]
}

final case class SelectionAction(name: String, `type`: String, value: ActionValue)

object SelectionAction {
  implicit val decoder: Decoder[SelectionAction] = deriveDecoder[SelectionAction]
  implicit val eEncoder: Encoder[SelectionAction] = deriveEncoder[SelectionAction]
}
