package com.dawidqb.haiku

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import com.dawidqb.haiku.repo.MemoryRepo
import com.typesafe.config.ConfigFactory
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze._

object Main extends IOApp {

  private val config = ConfigFactory.load()
  private val haikuRepo = new MemoryRepo
  private val haikuService = new HaikuService(haikuRepo)
  private val haikuRoutes = new HaikuRoutes(haikuService)
  private val adminRoutes = new AdminRoutes(haikuRepo)

  private val httpApp: Kleisli[IO, Request[IO], Response[IO]] =
    Router("/api" -> haikuRoutes.routes, "/api/admin" -> adminRoutes.routes).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(config.getInt("port"), config.getString("host"))
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
