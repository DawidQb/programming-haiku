package com.dawidqb.haiku.model

final case class HaikuData(
                            haiku: String,
                            ratings: Map[UserId, Int] = Map.empty
                          )
