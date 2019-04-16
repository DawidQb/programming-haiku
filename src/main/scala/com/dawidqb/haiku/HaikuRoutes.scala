package com.dawidqb.haiku

import cats.effect._
import cats.implicits._
import com.dawidqb.haiku.model.{SlackCommandRequest, SlackSelectRequest}
import com.typesafe.scalalogging.LazyLogging
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.{HttpRoutes, Uri}

class HaikuRoutes(haikuService: HaikuService)(implicit val cs: ContextShift[IO]) extends LazyLogging {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      logger.info("Received / request!")
      Ok("Hello!")

    case request@GET -> Root / "oauth" =>
      logger.info("Received OAuth request!")
      request.params.get("code") match {
        case None =>
          logger.warn(s"Failed OAuth! Params: ${request.params}")
          Found(Location(Uri.uri("/redirect")))
        case Some(code) =>
          logger.info("Received OAuth code!")
          haikuService.handleOauth(code) *>
            Found(Location(Uri.uri("/redirect")))
      }

    case request@POST -> Root / "haiku" =>
      for {
        req <- request.as[SlackCommandRequest]
        _ = logger.info(s"Received haiku request $req")
        res <- Ok(haikuService.handleCommand(req))
      } yield res

    case request@POST -> Root / "select" =>
      for {
        req <- request.as[SlackSelectRequest]
        _ = logger.info(s"Received select request $req")
        res <- Ok(haikuService.handleSelection(req))
      } yield res
  }

}
