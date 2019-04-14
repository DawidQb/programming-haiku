package com.dawidqb.haiku

import java.util.UUID

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuId, Language, SlackAttachment, SlackCommandRequest, SlackHaikuResponse, SlackSelectRequest}
import com.dawidqb.haiku.repo.HaikuRepo

class HaikuService(haikuRepo: HaikuRepo) {

  def createHaiku(language: Language): IO[SlackHaikuResponse] = for {
    haiku <- HaikuGenerator.generateHaiku(language)
    haikuId <- HaikuGenerator.generateHaikuId
    _ <- haikuRepo.insertHaiku(haikuId, haiku, language)
  } yield SlackHaikuResponse(haiku, SlackAttachment.attachmentForHaiku(haikuId, language))

  def handleCommand(request: SlackCommandRequest): IO[SlackHaikuResponse] =
    if (request.text.trim.isEmpty) {
      IO.pure(SlackHaikuResponse.MissingLanguage)
    } else {
      Language.withNameInsensitiveOption(request.text.trim) match {
        case None => IO.pure(SlackHaikuResponse.InvalidLanguage)
        case Some(lang) => createHaiku(lang)
      }
    }

  def handleSelection(request: SlackSelectRequest): IO[SlackHaikuResponse] = {
    val actionValue = request.actions.headOption.map(_.value).getOrElse("send")
    val haikuId = HaikuId(UUID.fromString(request.callback_id))
    actionValue match {
      case "send" =>
        for {
          haiku <- haikuRepo.findHaikuData(haikuId).map(_.map(_.haiku).getOrElse(""))
        } yield SlackHaikuResponse(haiku, Nil, delete_original = true, response_type = "in_channel")
      case "shuffle" =>
        for {
          language <- haikuRepo.findHaikuData(haikuId).map(_.map(_.language).getOrElse(Language.en))
          response <- createHaiku(language)
        } yield response
      case _ =>
        IO.pure(SlackHaikuResponse.DeleteOriginal)
    }
  }

}
