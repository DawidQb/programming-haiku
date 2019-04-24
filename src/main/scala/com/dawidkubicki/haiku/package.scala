package com.dawidkubicki

import java.time.Instant

import cats.data.EitherT
import cats.effect.IO

package object haiku {
  implicit class RichInstant(val value: Instant) extends Ordered[RichInstant] {
    override def compare(that: RichInstant): Int = this.value.compareTo(that.value)
  }

  implicit class EitherTOps[L, R](value: Either[L, R]) {
    def asEitherT: EitherT[IO, L, R] = EitherT.fromEither[IO](value)
  }

  implicit class EitherTOptionOps[L, R](value: Option[R]) {
    def asEitherT(ifNone: => L): EitherT[IO, L, R] = EitherT.fromOption[IO](value, ifNone)
  }
}
