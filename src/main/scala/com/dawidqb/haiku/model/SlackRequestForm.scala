package com.dawidqb.haiku.model

import cats.effect.IO
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackRequestForm(command: String, userName: String, userId: UserId)

object SlackRequestForm {

  def apply(urlForm: UrlForm): SlackRequestForm =
    SlackRequestForm(
      command = urlForm.values("command").headOption.getOrElse(""),
      userName = urlForm.values("user_name").headOption.getOrElse(""),
      userId = UserId(urlForm.values("user_id").headOption.getOrElse(""))
    )

  implicit val decoder: EntityDecoder[IO, SlackRequestForm] =
    UrlForm.entityDecoder[IO].map(SlackRequestForm.apply)

}