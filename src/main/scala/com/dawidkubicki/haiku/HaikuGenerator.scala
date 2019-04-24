package com.dawidkubicki.haiku

import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.{HaikuId, Language}

import scala.io.Source
import scala.util.Random

object HaikuGenerator {

  private val enFiveSyllablesLines = Source.fromResource("en/five_syllables.txt").getLines.toArray
  private val enSevenSyllablesLines = Source.fromResource("en/seven_syllables.txt").getLines.toArray

  private val plFiveSyllablesLines = Source.fromResource("pl/five_syllables.txt").getLines.toArray
  private val plSevenSyllablesLines = Source.fromResource("pl/seven_syllables.txt").getLines.toArray

  private def fiveSyllablesLines(language: Language) = language match {
    case Language.EN => enFiveSyllablesLines
    case Language.PL => plFiveSyllablesLines
  }

  private def sevenSyllablesLines(language: Language) = language match {
    case Language.EN => enSevenSyllablesLines
    case Language.PL => plSevenSyllablesLines
  }

  def generateHaiku(language: Language): IO[String] = IO {
    val fiveSyl = fiveSyllablesLines(language)
    val sevenSyl = sevenSyllablesLines(language)

    val first = fiveSyl(Random.nextInt(fiveSyl.length))
    val second = sevenSyl(Random.nextInt(sevenSyl.length))
    val third = fiveSyl(Random.nextInt(fiveSyl.length))
    List(first, second, third).mkString("\n")
  }

  val generateHaikuId: IO[HaikuId] = IO {
    HaikuId(UUID.randomUUID())
  }

}
