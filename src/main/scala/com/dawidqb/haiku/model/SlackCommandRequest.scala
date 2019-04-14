package com.dawidqb.haiku.model

import cats.effect.IO
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackCommandRequest(command: String, text: String, userName: String, userId: UserId)

object SlackCommandRequest {

  def apply(urlForm: UrlForm): SlackCommandRequest =
    SlackCommandRequest(
      command = urlForm.values("command").headOption.getOrElse(""),
      text = urlForm.values("text").headOption.getOrElse("").toLowerCase,
      userName = urlForm.values("user_name").headOption.getOrElse(""),
      userId = UserId(urlForm.values("user_id").headOption.getOrElse(""))
    )

  implicit val decoder: EntityDecoder[IO, SlackCommandRequest] =
    UrlForm.entityDecoder[IO].map(SlackCommandRequest.apply)

}