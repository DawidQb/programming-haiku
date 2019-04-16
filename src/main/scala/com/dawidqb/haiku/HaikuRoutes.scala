package com.dawidqb.haiku

import cats.effect._
import cats.implicits._
import com.dawidqb.haiku.model.{SlackCommandRequest, SlackSelectRequest}
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.{HttpRoutes, Uri}

class HaikuRoutes(haikuService: HaikuService)(implicit val cs: ContextShift[IO]) {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      println("Received request!")
      Ok("Hello!")

    case request@GET -> Root / "oauth" =>
      println("Received OAuth request!")
      request.params.get("code") match {
        case None =>
          println(s"Failed Oauth! Params: ${request.params}")
          Found(Location(Uri.uri("/redirect")))
        case Some(code) =>
          println("Successful Oauth!")
          haikuService.handleOauth(code)/*.map(println)*/ *>
            Found(Location(Uri.uri("/redirect")))
      }

    case request@POST -> Root / "haiku" =>
      for {
        req <- request.as[SlackCommandRequest]
        _ = println(s"Received haiku request $req")
        res <- Ok(haikuService.handleCommand(req))
      } yield res

    case request@POST -> Root / "select" =>
      for {
        req <- request.as[SlackSelectRequest]
        _ = println(s"Received select request $req")
        res <- Ok(haikuService.handleSelection(req))
      } yield res
  }

}
