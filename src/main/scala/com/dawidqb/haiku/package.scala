package com.dawidqb

import java.time.Instant

package object haiku {
  implicit class RichInstant(val value: Instant) extends Ordered[RichInstant] {
    override def compare(that: RichInstant): Int = this.value.compareTo(that.value)
  }
}
