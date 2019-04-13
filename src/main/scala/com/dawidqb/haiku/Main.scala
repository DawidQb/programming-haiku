package com.dawidqb.haiku

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import com.dawidqb.haiku.model.{HaikuId, SlackAction, SlackAttachment, SlackHaikuResponse, SlackRatingRequest, SlackRequestForm}
import com.dawidqb.haiku.repo.MemoryRepo
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze._


object Main extends IOApp {

  implicit val encoder: EntityEncoder[IO, SlackHaikuResponse] = jsonEncoderOf[IO, SlackHaikuResponse]

  private val haikuRepo = new MemoryRepo

  private val createHaiku: IO[SlackHaikuResponse] = for {
    haiku <- HaikuGenerator.generateHaiku
    haikuId <- HaikuGenerator.generateHaikuId
    _ <- haikuRepo.insertHaiku(haikuId, haiku)
  } yield SlackHaikuResponse(haiku, prepareAttachments(haikuId))

  private def prepareAttachments(haikuId: HaikuId): List[SlackAttachment] =
    List(SlackAttachment(
      text = "Oceń jak bardzo piękne jest powyższe haiku",
      fallback = "Nie jesteś w stanie ocenić haiku",
      callback_id = haikuId.value.toString,
      actions = actions
    ))

  private val actions: List[SlackAction] =
    List(SlackAction.greatRatingButton, SlackAction.mediocreRatingButton, SlackAction.badRatingButton)

  private val haikuService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case request@POST -> Root / "haiku" =>
      request.as[SlackRequestForm].map(println) *>
        Ok(createHaiku)

    case request@POST -> Root / "rate" =>
      println("trololololo")
      request.as[SlackRatingRequest] *>
        Ok("xD")
  }

  private val httpApp: Kleisli[IO, Request[IO], Response[IO]] = Router("/" -> haikuService).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(5000, "127.0.0.1")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
