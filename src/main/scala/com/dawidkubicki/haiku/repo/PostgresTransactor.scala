package com.dawidkubicki.haiku.repo

import cats.effect._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import doobie._
import doobie.hikari._

class PostgresTransactor(implicit cs: ContextShift[IO]) {

  private val dbConfig = ConfigFactory.load().getConfig("db")

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      te <- ExecutionContexts.cachedThreadPool[IO] // our transaction EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.getString("driver"),
        url = dbConfig.getString("url"),
        user = dbConfig.getString("user"),
        pass = dbConfig.getString("password"),
        ce, // await connection here
        te // execute JDBC operations here
      )
      _ = xa.configure(hds => IO {
        hds.setMaximumPoolSize(16)
      })
    } yield xa

}
