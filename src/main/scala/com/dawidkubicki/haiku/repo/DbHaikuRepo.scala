package com.dawidkubicki.haiku.repo

import java.time.Instant
import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.{DbMapping, HaikuData, HaikuId}
import com.dawidkubicki.haiku.model.Language
import doobie.hikari.HikariTransactor
import doobie.implicits._

class DbHaikuRepo(hikariTransactor: HikariTransactor[IO]) extends HaikuRepo with DbMapping {

  override def find(haikuId: HaikuId): IO[Option[HaikuData]] =
    sql"""
          SELECT id, haiku, language, "commandId", "createdAt"
          FROM haikus
          WHERE id = ${haikuId.value}::uuid
       """
      .query[HaikuData]
      .option
      .transact(hikariTransactor)

  override def insert(haiku: String, language: Language, commandId: UUID): IO[HaikuId] =
    sql"""
        INSERT INTO haikus (haiku, language, "commandId", "createdAt")
        VALUES ($haiku, ${language.entryName}, $commandId::uuid, ${Instant.now()})
      """
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(hikariTransactor)
      .map(HaikuId.apply)

  override def list(dateFrom: Option[Instant], dateTo: Option[Instant]): IO[List[HaikuData]] =
    sql"""
          SELECT id, haiku, language, "commandId", "createdAt"
          FROM haikus
          WHERE "createdAt" > $dateFrom AND "createdAt" < $dateTo
       """
      .query[HaikuData]
      .to[List]
      .transact(hikariTransactor)

}
