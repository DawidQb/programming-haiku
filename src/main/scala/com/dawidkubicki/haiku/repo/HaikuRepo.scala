package com.dawidkubicki.haiku.repo

import java.time.Instant

import cats.effect.IO
import com.dawidkubicki.haiku.model.{HaikuData, HaikuId, Language}

trait HaikuRepo {

  def findHaikuData(haikuId: HaikuId): IO[Option[HaikuData]]

  def insertHaiku(haikuId: HaikuId, haiku: String, language: Language): IO[Unit]

  def listHaikus(dateFrom: Option[Instant], dateTo: Option[Instant]): IO[List[HaikuData]]

}
