package com.dawidqb.haiku.repo

import cats.effect.IO
import com.dawidqb.haiku.model.HaikuId

trait HaikuRepo {

  def findHaiku(haikuId: HaikuId): IO[Option[String]]

  def insertHaiku(haikuId: HaikuId, haiku: String): IO[Unit]

}
