package com.dawidkubicki.haiku

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import com.dawidkubicki.haiku.repo.{DbCommandRepo, DbHaikuEventRepo, DbHaikuRepo, PostgresTransactor}
import com.typesafe.config.ConfigFactory
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze._

object Main extends IOApp {

  private val config = ConfigFactory.load()
  private val postgresTransactor = new PostgresTransactor

  override def run(args: List[String]): IO[ExitCode] = {
    postgresTransactor.transactor.use { hikariTransactor =>
      val haikuRepo = new DbHaikuRepo(hikariTransactor)
      val haikuEventRepo = new DbHaikuEventRepo(hikariTransactor)
      val commandRepo = new DbCommandRepo(hikariTransactor)
      val haikuService = new HaikuService(haikuRepo, haikuEventRepo, commandRepo)
      val haikuRoutes = new HaikuRoutes(haikuService)
      val adminRoutes = new AdminRoutes(haikuRepo)

      val httpApp: Kleisli[IO, Request[IO], Response[IO]] =
      Router("/api" -> haikuRoutes.routes, "/api/admin" -> adminRoutes.routes).orNotFound

      BlazeServerBuilder[IO]
        .bindHttp(config.getInt("port"), config.getString("host"))
        .withHttpApp(httpApp)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    }
  }

}
