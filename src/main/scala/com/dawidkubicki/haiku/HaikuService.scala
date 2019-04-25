package com.dawidkubicki.haiku

import java.time.Instant
import java.util.UUID

import cats.effect.{ContextShift, IO}
import cats.implicits._
import com.dawidkubicki.haiku.model.ResponseType.InChannel
import com.dawidkubicki.haiku.model._
import com.dawidkubicki.haiku.model.db.{Command, EmptyUUID, HaikuEvent, HaikuId}
import com.dawidkubicki.haiku.repo.{CommandRepo, HaikuEventRepo, HaikuRepo}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io._
import org.http4s.{Uri, UrlForm}

import scala.concurrent.ExecutionContext.global

class HaikuService(haikuRepo: HaikuRepo, haikuEventRepo: HaikuEventRepo, commandRepo: CommandRepo)
  extends Http4sClientDsl[IO] with LazyLogging {

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
    BlazeClientBuilder[IO](global).resource.use {
      _.expect[String](request)
    }
  }

  def handleCommand(request: SlackCommandRequest): IO[SlackHaikuResponse] = {
    logger.info(s"Handling SlackCommandRequest $request")
    for {
      commandId <- commandRepo.insert(Command.fromRequest(request))
      res <- if (request.text.trim.isEmpty) {
        IO.pure(SlackHaikuResponse.MissingLanguage)
      } else {
        Language.withNameInsensitiveOption(request.text.trim) match {
          case None => IO.pure(SlackHaikuResponse.InvalidLanguage)
          case Some(lang) => createHaiku(lang, commandId)
        }
      }
    } yield res
  }

  def handleSelection(request: SlackSelectRequest): IO[SlackHaikuResponse] = {
    logger.info(s"Handling SlackSelectRequest $request")
    val actionValue = request.actions.headOption.map(_.value).getOrElse(ActionValue.Cancel)
    val haikuId = HaikuId(UUID.fromString(request.callback_id))
    haikuEventRepo.insert(HaikuEvent(haikuId, "selection", actionValue.entryName)) *>
      (actionValue match {
        case ActionValue.Send =>
          for {
            haiku <- haikuRepo.find(haikuId).map(_.map(_.haiku).getOrElse(""))
          } yield SlackHaikuResponse(haiku, Nil, delete_original = true, response_type = InChannel)
        case ActionValue.Shuffle =>
          for {
            haikuDataOpt <- haikuRepo.find(haikuId)
            language = haikuDataOpt.map(_.language).getOrElse(Language.EN)
            commandId = haikuDataOpt.map(_.commandId).getOrElse(EmptyUUID.value)
            response <- createHaiku(language, commandId)
          } yield response
        case ActionValue.Cancel =>
          IO.pure(SlackHaikuResponse.DeleteOriginal)
      })
  }

  private def createHaiku(language: Language, commandId: UUID): IO[SlackHaikuResponse] = for {
    haiku <- HaikuGenerator.generateHaiku(language)
    haikuId <- haikuRepo.insert(haiku, language, commandId)
  } yield SlackHaikuResponse(haiku, SlackAttachment.attachmentForHaiku(haikuId, language))


}
