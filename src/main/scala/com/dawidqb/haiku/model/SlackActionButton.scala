package com.dawidqb.haiku.model

final case class SlackActionButton(
                                    name: String,
                                    text: String,
                                    value: String,
                                    action_id: String,
                                    style: Option[String] = None,
                                    `type`: String = "button",
                                  )

object SlackActionButton {

  private val sendText = "Wyślij \uD83D\uDE0A"
  private val shuffleText = "Wygeneruj nowe \uD83D\uDD04"
  private val cancelText = "Anuluj ❌"

  val sendButton = SlackActionButton("selection", sendText, "send", "1", Some("primary"))
  val shuffleButton = SlackActionButton("selection", shuffleText, "shuffle", "2")
  val cancelButton = SlackActionButton("selection", cancelText, "cancel", "3", Some("danger"))

}
