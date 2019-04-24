package com.dawidkubicki.haiku.model

import java.time.Instant

final case class HaikuData(
                            haiku: String,
                            language: Language,
                            createdAt: Instant
                          )
