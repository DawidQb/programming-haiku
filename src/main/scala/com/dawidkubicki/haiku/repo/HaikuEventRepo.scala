package com.dawidkubicki.haiku.repo

import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.HaikuEvent

trait HaikuEventRepo {
  def insert(event: HaikuEvent): IO[UUID]
}
