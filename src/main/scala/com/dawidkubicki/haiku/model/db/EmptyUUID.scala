package com.dawidkubicki.haiku.model.db

import java.util.UUID

case object EmptyUUID {

  val value: UUID = new UUID(0, 0)

  implicit def emptyUUID2UUID(empty: EmptyUUID.type): UUID = value

}
