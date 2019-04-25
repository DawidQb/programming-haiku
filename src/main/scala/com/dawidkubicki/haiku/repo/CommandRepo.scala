package com.dawidkubicki.haiku.repo

import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.Command

trait CommandRepo {
  def insert(command: Command): IO[UUID]
}
