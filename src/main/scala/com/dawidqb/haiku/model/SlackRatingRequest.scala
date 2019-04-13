package com.dawidqb.haiku.model

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.parser._
import org.http4s.{EntityDecoder, UrlForm}

final case class SlackRatingRequest(
                                     callback_id: String,
                                     original_message: SlackHaikuResponse
                                   )

object SlackRatingRequest {

  implicit val decoder: EntityDecoder[IO, SlackRatingRequest] =
    UrlForm.entityDecoder[IO].map { urlForm =>
      parse(urlForm.values("payload").head).right.get.as[SlackRatingRequest].right.get
    }

}

final case class RatingUser(id: UserId, name: String)
