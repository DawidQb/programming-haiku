package com.dawidqb.haiku.model

final case class SlackAttachment(
                                  text: String,
                                  fallback: String,
                                  callback_id: String,
                                  actions: List[SlackActionButton],
                                  color: String = "#3AA3E3"
                                )

object SlackAttachment {

  private val selectionActions: List[SlackActionButton] =
    List(SlackActionButton.sendButton, SlackActionButton.shuffleButton, SlackActionButton.cancelButton)

  def attachmentForHaiku(haikuId: HaikuId): List[SlackAttachment] =
    List(SlackAttachment(
      text = "Czy powyższe haiku jest odpowiednie do wysłania?",
      fallback = "Nie jesteś w stanie wybrać haiku",
      callback_id = haikuId.value.toString,
      actions = selectionActions
    ))

}