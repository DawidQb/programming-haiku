package com.dawidkubicki.haiku.repo

import java.util.UUID

import cats.effect.IO
import com.dawidkubicki.haiku.model.db.{Command, DbMapping}
import doobie.hikari.HikariTransactor
import doobie._
import doobie.implicits._

class DbCommandRepo(hikariTransactor: HikariTransactor[IO]) extends CommandRepo with DbMapping {
  override def insert(command: Command): IO[UUID] = {
    val c = command
    import c._
    sql"""
        INSERT INTO commands (command, "text", "userId", "userName", "teamId", "teamDomain", "responseUrl", "createdAt")
        VALUES (${c.command}, $text, ${userId.value}, $userName, $teamId, $teamDomain, $responseUrl, $createdAt)
      """
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(hikariTransactor)
  }
}
