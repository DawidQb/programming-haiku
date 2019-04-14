package com.dawidqb.haiku.model

import com.typesafe.config.ConfigFactory

final case class SlackAttachment(
                                  text: String,
                                  fallback: String,
                                  callback_id: String,
                                  actions: List[SlackActionButton],
                                  color: String = "#3AA3E3"
                                )

object SlackAttachment {

  private val messagesConfig = ConfigFactory.load().getConfig("messages")
  private def configForLanguage(language: Language) = messagesConfig.getConfig(language.entryName)

  private def selectionActions(language: Language): List[SlackActionButton] =
    List(
      SlackActionButton.sendButton(language),
      SlackActionButton.shuffleButton(language),
      SlackActionButton.cancelButton(language)
    )

  def attachmentForHaiku(haikuId: HaikuId, language: Language): List[SlackAttachment] =
    List(SlackAttachment(
      text = configForLanguage(language).getString("question"),
      fallback = configForLanguage(language).getString("question-fallback"),
      callback_id = haikuId.value.toString,
      actions = selectionActions(language)
    ))

}