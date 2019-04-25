package com.dawidkubicki.haiku.model.db

import java.time.Instant
import java.util.UUID

final case class HaikuEvent(
                             id: UUID,
                             haikuId: HaikuId,
                             action: String,
                             value: String,
                             createdAt: Instant
                           )

object HaikuEvent {

  def apply(haikuId: HaikuId, action: String, value: String): HaikuEvent =
    HaikuEvent(EmptyUUID, haikuId, action, value, Instant.now())

}