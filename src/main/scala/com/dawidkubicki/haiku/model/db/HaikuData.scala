package com.dawidkubicki.haiku.model.db

import java.time.Instant
import java.util.UUID

import com.dawidkubicki.haiku.model.Language

final case class HaikuData(
                            id: HaikuId,
                            haiku: String,
                            language: Language,
                            commandId: UUID,
                            createdAt: Instant
                          )
