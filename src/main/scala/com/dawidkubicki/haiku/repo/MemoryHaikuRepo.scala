package com.dawidkubicki.haiku.repo

import java.time.Instant
import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku._
import com.dawidkubicki.haiku.model.Language
import com.dawidkubicki.haiku.model.db.{HaikuData, HaikuId}

class MemoryHaikuRepo extends HaikuRepo {

  import MemoryHaikuRepo._

  override def insert(haiku: String, language: Language, commandId: UUID): IO[HaikuId] = IO {
    val haikuId = HaikuId(UUID.randomUUID())
    memory += haikuId -> HaikuData(haikuId, haiku, language, commandId, Instant.now())
    haikuId
  }

  override def find(haikuId: HaikuId): IO[Option[HaikuData]] = IO {
    memory.get(haikuId)
  }

  override def list(dateFrom: Option[Instant], dateTo: Option[Instant]): IO[List[HaikuData]] = IO {
    memory.values
      .filter(h => dateFrom.fold(true)(h.createdAt > _))
      .filter(h => dateTo.fold(true)(h.createdAt < _))
      .toList
  }

}

object MemoryHaikuRepo {

  private val memory = scala.collection.mutable.Map[HaikuId, HaikuData]()
}
