package com.dawidkubicki.haiku.model

import java.time.Instant

import cats.effect.IO
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.circe._


final case class AdminRequest(
                                dateFrom: Option[Instant],
                                dateTo: Option[Instant],
                                file: Option[String]
                             )

object AdminRequest {

  implicit val decoder: Decoder[AdminRequest] = deriveDecoder[AdminRequest]
  implicit val encoder: Encoder[AdminRequest] = deriveEncoder[AdminRequest]

  implicit val entityEncoder = jsonEncoderOf[IO, AdminRequest]
  implicit val entityDecoder = jsonDecoder[IO].map(_.as[AdminRequest].right.get)


}
