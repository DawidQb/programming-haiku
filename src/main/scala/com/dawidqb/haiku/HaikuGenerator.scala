package com.dawidqb.haiku

import java.util.UUID

import cats.effect.IO
import com.dawidqb.haiku.model.HaikuId

import scala.io.Source
import scala.util.Random

object HaikuGenerator {

  private val fiveSyllablesLines = Source.fromResource("five_syllables.txt").getLines.toList
  private val sevenSyllablesLines = Source.fromResource("seven_syllables.txt").getLines.toList

  val generateHaiku: IO[String] = IO {
    val first = fiveSyllablesLines(Random.nextInt(fiveSyllablesLines.size))
    val second = sevenSyllablesLines(Random.nextInt(sevenSyllablesLines.size))
    val third = fiveSyllablesLines(Random.nextInt(fiveSyllablesLines.size))
    List(first, second, third).mkString("\n")
  }

  val generateHaikuId: IO[HaikuId] = IO {
    HaikuId(UUID.randomUUID())
  }

}
