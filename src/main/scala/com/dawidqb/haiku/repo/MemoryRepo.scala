package com.dawidqb.haiku.repo

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuData, HaikuId}

class MemoryRepo {

  import MemoryRepo._

  def insertHaiku(haikuId: HaikuId, haiku: String): IO[Unit] = IO {
    if (!memory.contains(haikuId)) memory += haikuId -> HaikuData(haiku)
  }


}

object MemoryRepo {
  private val memory = scala.collection.mutable.Map[HaikuId, HaikuData]()
}
