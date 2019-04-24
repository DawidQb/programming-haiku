package com.dawidkubicki.haiku

import java.util.UUID

import cats.effect.{ContextShift, IO}
import com.dawidkubicki.haiku.model.ResponseType.InChannel
import com.dawidkubicki.haiku.model._
import com.dawidkubicki.haiku.repo.HaikuRepo
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io._
import org.http4s.{Uri, UrlForm}

import scala.concurrent.ExecutionContext.global

class HaikuService(haikuRepo: HaikuRepo) extends Http4sClientDsl[IO] with LazyLogging {

  private val config = ConfigFactory.load()
  private val clientId = config.getString("client-id")
  private val clientSecret = config.getString("client-secret")

  def handleOauth(code: String)(implicit cs: ContextShift[IO]): IO[String] = {
    val uri = Uri.uri("https://slack.com/api/oauth.access")
    val body = UrlForm(
      "client_id" -> clientId,
      "client_secret" -> clientSecret,
      "code" -> code
    )
    val request = POST(uri, body)
    BlazeClientBuilder[IO](global).resource.use { _.expect[String](request) }
  }

  def handleCommand(request: SlackCommandRequest): IO[SlackHaikuResponse] = {
    logger.info(s"Handling SlackCommandRequest $request")
    if (request.text.trim.isEmpty) {
      IO.pure(SlackHaikuResponse.MissingLanguage)
    } else {
      Language.withNameInsensitiveOption(request.text.trim) match {
        case None => IO.pure(SlackHaikuResponse.InvalidLanguage)
        case Some(lang) => createHaiku(lang)
      }
    }
  }

  def handleSelection(request: SlackSelectRequest): IO[SlackHaikuResponse] = {
    logger.info(s"Handling SlackSelectRequest $request")
    val actionValue = request.actions.headOption.map(_.value).getOrElse(ActionValue.Cancel)
    val haikuId = HaikuId(UUID.fromString(request.callback_id))
    actionValue match {
      case ActionValue.Send =>
        for {
          haiku <- haikuRepo.findHaikuData(haikuId).map(_.map(_.haiku).getOrElse(""))
        } yield SlackHaikuResponse(haiku, Nil, delete_original = true, response_type = InChannel)
      case ActionValue.Shuffle =>
        for {
          language <- haikuRepo.findHaikuData(haikuId).map(_.map(_.language).getOrElse(Language.EN))
          response <- createHaiku(language)
        } yield response
      case ActionValue.Cancel =>
        IO.pure(SlackHaikuResponse.DeleteOriginal)
    }
  }

  private def createHaiku(language: Language): IO[SlackHaikuResponse] = for {
    haiku <- HaikuGenerator.generateHaiku(language)
    haikuId <- HaikuGenerator.generateHaikuId
    _ <- haikuRepo.insertHaiku(haikuId, haiku, language)
  } yield SlackHaikuResponse(haiku, SlackAttachment.attachmentForHaiku(haikuId, language))


}