package com.dawidkubicki.haiku.repo

import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.{DbMapping, HaikuEvent}
import doobie.hikari.HikariTransactor
import doobie.implicits._

class DbHaikuEventRepo(hikariTransactor: HikariTransactor[IO]) extends HaikuEventRepo with DbMapping {
  override def insert(event: HaikuEvent): IO[UUID] = {
    import event._
    sql"""
        INSERT INTO haiku_events ("haikuId", "action", "value", "createdAt")
        VALUES ($haikuId::uuid, $action, $value, $createdAt)
      """
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(hikariTransactor)
  }
}
