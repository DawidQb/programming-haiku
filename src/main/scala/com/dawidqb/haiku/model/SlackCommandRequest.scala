package com.dawidqb.haiku.model

import cats.effect.IO
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackCommandRequest(
                                      command: String,
                                      text: String,
                                      userId: UserId,
                                      userName: String,
                                      teamId: String,
                                      teamDomain: String,
                                      responseUrl: String
                                    )

object SlackCommandRequest {

  def apply(urlForm: UrlForm): SlackCommandRequest =
    SlackCommandRequest(
      command = urlForm.values("command").headOption.getOrElse(""),
      text = urlForm.values("text").headOption.getOrElse("").toLowerCase,
      userId = UserId(urlForm.values("user_id").headOption.getOrElse("")),
      userName = urlForm.values("user_name").headOption.getOrElse(""),
      teamId = urlForm.values("team_id").headOption.getOrElse(""),
      teamDomain = urlForm.values("team_domain").headOption.getOrElse(""),
      responseUrl = urlForm.values("response_url").headOption.getOrElse("")
    )

  implicit val decoder: EntityDecoder[IO, SlackCommandRequest] =
    UrlForm.entityDecoder[IO].map(SlackCommandRequest.apply)

}