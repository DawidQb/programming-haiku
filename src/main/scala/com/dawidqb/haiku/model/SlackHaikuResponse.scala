package com.dawidqb.haiku.model

final case class SlackHaikuResponse(
                                     text: String,
                                     attachments: List[SlackAttachment],
                                     response_type: Option[String] = Some("in_channel")
                                   )

final case class SlackAttachment(
                                  text: String,
                                  fallback: String,
                                  callback_id: String,
                                  actions: List[SlackAction],
                                  color: String = "#3AA3E3"
                                )

final case class SlackAction(
                              name: String,
                              text: String,
                              value: String,
                              style: Option[String] = None,
                              `type`: String = "button",
                            )

object SlackAction {

  private val greatText = "Wspaniałe 😊"
  private val mediocreText = "Takie sobie 😐"
  private val badText = "Słabizna 🙄"

  val greatRatingButton = SlackAction("rating", greatText, "great", Some("primary"))
  val mediocreRatingButton = SlackAction("rating", mediocreText, "mediocre")
  val badRatingButton = SlackAction("rating", badText, "bad", Some("danger"))

}
