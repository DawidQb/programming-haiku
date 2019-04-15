package com.dawidqb.haiku

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import com.dawidqb.haiku.model._
import com.dawidqb.haiku.repo.MemoryRepo
import com.typesafe.config.ConfigFactory
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.server.Router
import org.http4s.server.blaze._

object Main extends IOApp {

  private val config = ConfigFactory.load()
  private val haikuRepo = new MemoryRepo
  private val haikuService = new HaikuService(haikuRepo)

  private val haikuRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      println("Received request!")
      Ok("Hello!")

    case GET -> Root / "oauth" =>
      println("Received OAuth request!")
      Found(Location(Uri.uri("/redirect")))

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

  private val httpApp: Kleisli[IO, Request[IO], Response[IO]] = Router("/api" -> haikuRoutes).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(config.getInt("port"), config.getString("host"))
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
