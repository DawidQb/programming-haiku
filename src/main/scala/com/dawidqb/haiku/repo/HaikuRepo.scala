package com.dawidqb.haiku.repo

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuData, HaikuId, Language}

trait HaikuRepo {

  def findHaikuData(haikuId: HaikuId): IO[Option[HaikuData]]

  def insertHaiku(haikuId: HaikuId, haiku: String, language: Language): IO[Unit]

}
