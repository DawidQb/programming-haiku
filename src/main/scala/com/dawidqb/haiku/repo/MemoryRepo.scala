package com.dawidqb.haiku.repo

import java.time.Instant

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuData, HaikuId, Language}

class MemoryRepo extends HaikuRepo {

  import MemoryRepo._

  override def insertHaiku(haikuId: HaikuId, haiku: String, language: Language): IO[Unit] = IO {
    if (!memory.contains(haikuId)) memory += haikuId -> HaikuData(haiku, language, Instant.now())
  }

  override def findHaikuData(haikuId: HaikuId): IO[Option[HaikuData]] = IO {
    memory.get(haikuId)
  }


}

object MemoryRepo {
  private val memory = scala.collection.mutable.Map[HaikuId, HaikuData]()
}
