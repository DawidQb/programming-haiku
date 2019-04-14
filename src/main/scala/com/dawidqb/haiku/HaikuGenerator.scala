package com.dawidqb.haiku

import java.util.UUID

import cats.effect.IO
import com.dawidqb.haiku.model.{HaikuId, Language}

import scala.io.Source
import scala.util.Random

object HaikuGenerator {

  private val enFiveSyllablesLines = Source.fromResource("en/five_syllables.txt").getLines.toList
  private val enSevenSyllablesLines = Source.fromResource("en/seven_syllables.txt").getLines.toList

  private val plFiveSyllablesLines = Source.fromResource("pl/five_syllables.txt").getLines.toList
  private val plSevenSyllablesLines = Source.fromResource("pl/seven_syllables.txt").getLines.toList

  private def fiveSyllablesLines(language: Language) = language match {
    case Language.`en` => enFiveSyllablesLines
    case Language.`pl` => plFiveSyllablesLines
  }

  private def sevenSyllablesLines(language: Language) = language match {
    case Language.`en` => enSevenSyllablesLines
    case Language.`pl` => plSevenSyllablesLines
  }

  def generateHaiku(language: Language): IO[String] = IO {
    val fiveSyl = fiveSyllablesLines(language)
    val sevenSyl = sevenSyllablesLines(language)

    val first = fiveSyl(Random.nextInt(fiveSyl.size))
    val second = sevenSyl(Random.nextInt(sevenSyl.size))
    val third = fiveSyl(Random.nextInt(fiveSyl.size))
    List(first, second, third).mkString("\n")
  }

  val generateHaikuId: IO[HaikuId] = IO {
    HaikuId(UUID.randomUUID())
  }

}
