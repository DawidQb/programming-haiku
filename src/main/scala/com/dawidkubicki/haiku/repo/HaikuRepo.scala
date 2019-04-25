package com.dawidkubicki.haiku.repo

import java.time.Instant
import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.{HaikuData, HaikuId}
import com.dawidkubicki.haiku.model.Language

trait HaikuRepo {

  def find(haikuId: HaikuId): IO[Option[HaikuData]]

  def insert(haiku: String, language: Language, commandId: UUID): IO[HaikuId]

  def list(dateFrom: Option[Instant], dateTo: Option[Instant]): IO[List[HaikuData]]

}
