package com.dawidqb.haiku.repo

import java.time.Instant

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuData, HaikuId, Language}
import com.dawidqb.haiku._

class MemoryRepo extends HaikuRepo {

  import MemoryRepo._

  override def insertHaiku(haikuId: HaikuId, haiku: String, language: Language): IO[Unit] = IO {
    if (!memory.contains(haikuId)) memory += haikuId -> HaikuData(haiku, language, Instant.now())
  }

  override def findHaikuData(haikuId: HaikuId): IO[Option[HaikuData]] = IO {
    memory.get(haikuId)
  }

  override def listHaikus(dateFrom: Option[Instant], dateTo: Option[Instant]): IO[List[HaikuData]] = IO {
    memory.values
      .filter(h => dateFrom.fold(true)(h.createdAt > _))
      .filter(h => dateTo.fold(true)(h.createdAt < _))
      .toList
  }

}

object MemoryRepo {

  private val memory = scala.collection.mutable.Map[HaikuId, HaikuData]()
}
