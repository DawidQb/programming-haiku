package com.dawidkubicki.haiku

import cats.data._
import cats.effect._
import com.dawidkubicki.haiku.model.AdminRequest
import com.dawidkubicki.haiku.repo.HaikuRepo
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

  private val middleware: AuthMiddleware[IO, Unit] = AuthMiddleware(authUser, onFailure)

  val routes: ReaderT[OptionT[IO, ?], Request[IO], Response[IO]] = middleware(AuthedService {
    case request@POST -> Root / "write" as _ =>
      for {
        req <- request.req.as[AdminRequest]
        _ = logger.info(s"Received admin haikus request! $req")
        res <- req.file match {
          case None =>
            Ok(haikuRepo.listHaikus(req.dateFrom, req.dateTo).map(_.map(_.haiku).mkString("\n\n")))
          case Some(file) =>

            Ok(haikuRepo.listHaikus(req.dateFrom, req.dateTo).map(_.map(_.haiku).mkString("\n\n")))
        }

      } yield res
  })

}
