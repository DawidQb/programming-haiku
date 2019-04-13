package com.dawidqb.haiku.model

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.parser._
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackSelectRequest(
                                     callback_id: String,
                                     user: SelectionUser,
                                     actions: List[SelectionAction],
                                     response_url: String
                                   )

object SlackSelectRequest {

  implicit val decoder: EntityDecoder[IO, SlackSelectRequest] =
    UrlForm.entityDecoder[IO].map { urlForm =>
      parse(urlForm.values("payload").head).right.get.as[SlackSelectRequest].right.get
    }

}

final case class SelectionUser(id: UserId, name: String)

final case class SelectionAction(name: String, `type`: String, value: String)
