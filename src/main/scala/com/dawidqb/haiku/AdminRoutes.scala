package com.dawidqb.haiku

import cats.data._
import cats.effect._
import com.dawidqb.haiku.repo.HaikuRepo
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server._
import org.http4s.util.CaseInsensitiveString

class AdminRoutes(haikuRepo: HaikuRepo) extends LazyLogging {

  private val adminToken = ConfigFactory.load().getString("admin-token")

  private val authUser: Kleisli[IO, Request[IO], Either[String, Unit]] = Kleisli { request =>
    IO.pure(for {
      header <- request.headers.get(CaseInsensitiveString("Admin-Token")).toRight("Couldn't find a token header")
      _ <- Either.cond(header.value == adminToken, (), "Invalid token")
    } yield ())
  }
  private val onFailure: AuthedService[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))

  val middleware: AuthMiddleware[IO, Unit] = AuthMiddleware(authUser, onFailure)

  val routes = middleware(AuthedService {
      case GET -> Root / "haikus" as _ =>
        logger.info("Received admin haikus request!")
        haikuRepo.listHaikus(None, None)
          .map(_.map(_.haiku).mkString("\n\n"))
          .flatMap(Ok(_))
    })

}
