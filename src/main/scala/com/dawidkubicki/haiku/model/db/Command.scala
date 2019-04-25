package com.dawidkubicki.haiku.model.db

import java.time.Instant
import java.util.UUID

import com.dawidkubicki.haiku.model.{SlackCommandRequest, UserId}

final case class Command(
                          id: UUID = EmptyUUID,
                          command: String,
                          text: String,
                          userId: UserId,
                          userName: String,
                          teamId: String,
                          teamDomain: String,
                          responseUrl: String,
                          createdAt: Instant = Instant.now()
                        )

object Command {
  def fromRequest(request: SlackCommandRequest): Command =
    Command(
      command = request.command,
      text = request.text,
      userId = request.userId,
      userName = request.userName,
      teamId = request.teamId,
      teamDomain = request.teamDomain,
      responseUrl = request.responseUrl
    )
}