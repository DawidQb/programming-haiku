package com.dawidkubicki.haiku

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.util.Random

class HaikuGenerationSimulation extends Simulation {

  private def haikuBody(lang: String) = s"command=/haiku&text=$lang&user_id=U1&user_name=Gatling&" +
    "response_url=http://zombo.com&team_id=T1&team_domain=Gatling"

  private val languages = List("pl", "PL", "en", "EN", "xd")

  private val feeder = Iterator.continually(Map("lang" -> languages(Random.nextInt(languages.size))))

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://269ff330.ngrok.io/api")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
    .contentTypeHeader("application/x-www-form-urlencoded")

  val scn: ScenarioBuilder = scenario("GenerateHaikuSimulation")
    .feed(feeder)
    .exec(
      http("haiku_request")
        .post("/haiku")
        .body(StringBody(session => haikuBody(session("lang").as[String])))
    )
    .pause(5.seconds)

  setUp(
    scn.inject(rampUsers(100) during 10.seconds)
  ).protocols(httpProtocol)
}
