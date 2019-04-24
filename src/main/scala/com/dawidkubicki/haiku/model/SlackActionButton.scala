package com.dawidkubicki.haiku.model

import com.dawidkubicki.haiku.model.ActionValue.{Cancel, Send, Shuffle}
import com.typesafe.config.ConfigFactory
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class SlackActionButton(
                                    name: String,
                                    text: String,
                                    value: ActionValue,
                                    action_id: String,
                                    style: Option[String] = None,
                                    `type`: String = "button",
                                  )

object SlackActionButton {

  implicit val decoder: Decoder[SlackActionButton] = deriveDecoder[SlackActionButton]
  implicit val encoder: Encoder[SlackActionButton] = deriveEncoder[SlackActionButton]

  private val messagesConfig = ConfigFactory.load().getConfig("messages")
  private def configForLanguage(language: Language) = messagesConfig.getConfig(language.entryName)

  def sendButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("send"), Send, "1", Some("primary"))
  def shuffleButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("shuffle"), Shuffle, "2")
  def cancelButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("cancel"), Cancel, "3", Some("danger"))

}
