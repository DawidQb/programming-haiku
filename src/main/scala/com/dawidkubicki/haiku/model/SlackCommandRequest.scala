package com.dawidkubicki.haiku.model

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

  implicit class UrlFormOps(urlForm: UrlForm) {
    def getOrEmptyString(key: String): String =
      urlForm.values.get(key).toList.flatten.headOption.getOrElse("")
  }

  def apply(urlForm: UrlForm): SlackCommandRequest =
    SlackCommandRequest(
      command = urlForm.getOrEmptyString("command"),
      text = urlForm.getOrEmptyString("text").toLowerCase,
      userId = UserId(urlForm.getOrEmptyString("user_id")),
      userName = urlForm.getOrEmptyString("user_name"),
      teamId = urlForm.getOrEmptyString("team_id"),
      teamDomain = urlForm.getOrEmptyString("team_domain"),
      responseUrl = urlForm.getOrEmptyString("response_url")
    )

  implicit val decoder: EntityDecoder[IO, SlackCommandRequest] =
    UrlForm.entityDecoder[IO].map(SlackCommandRequest.apply)

}