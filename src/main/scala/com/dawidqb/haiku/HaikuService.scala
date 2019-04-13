package com.dawidqb.haiku

import java.util.UUID

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuId, SlackAttachment, SlackHaikuResponse, SlackSelectRequest}
import com.dawidqb.haiku.repo.HaikuRepo

class HaikuService(haikuRepo: HaikuRepo) {

  val createHaiku: IO[SlackHaikuResponse] = for {
    haiku <- HaikuGenerator.generateHaiku
    haikuId <- HaikuGenerator.generateHaikuId
    _ <- haikuRepo.insertHaiku(haikuId, haiku)
  } yield SlackHaikuResponse(haiku, SlackAttachment.attachmentForHaiku(haikuId))

  def handleSelection(request: SlackSelectRequest): IO[SlackHaikuResponse] = {
    val actionValue = request.actions.headOption.map(_.value).getOrElse("send")
    val haikuId = HaikuId(UUID.fromString(request.callback_id))
    actionValue match {
      case "send" =>
        for {
          haiku <- haikuRepo.findHaiku(haikuId).map(_.getOrElse(""))
        } yield SlackHaikuResponse(haiku, Nil, delete_original = true, response_type = "in_channel")
      case "shuffle" =>
        createHaiku
      case _ =>
        IO.pure(SlackHaikuResponse.DeleteOriginal)
    }
  }

}
