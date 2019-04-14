package com.dawidqb.haiku.model

import com.typesafe.config.ConfigFactory

final case class SlackActionButton(
                                    name: String,
                                    text: String,
                                    value: String,
                                    action_id: String,
                                    style: Option[String] = None,
                                    `type`: String = "button",
                                  )

object SlackActionButton {

  private val messagesConfig = ConfigFactory.load().getConfig("messages")
  private def configForLanguage(language: Language) = messagesConfig.getConfig(language.entryName)

  def sendButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("send"), "send", "1", Some("primary"))
  def shuffleButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("shuffle"), "shuffle", "2")
  def cancelButton(language: Language) =
    SlackActionButton("selection", configForLanguage(language).getString("cancel"), "cancel", "3", Some("danger"))

}
