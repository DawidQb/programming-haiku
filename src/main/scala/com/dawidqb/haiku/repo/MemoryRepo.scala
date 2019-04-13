package com.dawidqb.haiku.repo

import java.time.Instant

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuData, HaikuId}

class MemoryRepo extends HaikuRepo {

  import MemoryRepo._

  override def insertHaiku(haikuId: HaikuId, haiku: String): IO[Unit] = IO.pure {
    if (!memory.contains(haikuId)) memory += haikuId -> HaikuData(haiku, Instant.now())
  }

  override def findHaiku(haikuId: HaikuId): IO[Option[String]] = IO.pure {
    memory.get(haikuId).map(_.haiku)
  }


}

object MemoryRepo {
  private val memory = scala.collection.mutable.Map[HaikuId, HaikuData]()
}
