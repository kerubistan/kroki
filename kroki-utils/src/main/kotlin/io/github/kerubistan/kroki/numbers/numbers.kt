package io.github.kerubistan.kroki.numbers

import java.math.BigDecimal
import java.math.BigInteger

/*
 BigDecmal
 */

operator fun BigDecimal.times(nr : BigInteger) = this.times(nr.toBigDecimal())

operator fun BigDecimal.times(nr : Int) = this.times(nr.toBigDecimal())

operator fun BigDecimal.times(nr : Long) = this.times(nr.toBigDecimal())

operator fun BigDecimal.times(nr : Float) = this.times(nr.toBigDecimal())

operator fun BigDecimal.times(nr : Double) = this.times(nr.toBigDecimal())

operator fun BigDecimal.times(nr : Short) = this.times(nr.toInt().toBigDecimal())

operator fun BigDecimal.times(nr : Byte) = this.times(nr.toInt().toBigDecimal())

operator fun BigDecimal.plus(nr : Int) = this.plus(nr.toBigDecimal())

operator fun BigDecimal.plus(nr : Long) = this.plus(nr.toBigDecimal())

operator fun BigDecimal.plus(nr : Short) = this.plus(nr.toInt().toBigDecimal())

operator fun BigDecimal.plus(nr : Byte) = this.plus(nr.toInt().toBigDecimal())

operator fun BigDecimal.plus(nr : Float) = this.plus(nr.toBigDecimal())

operator fun BigDecimal.plus(nr : Double) = this.plus(nr.toBigDecimal())

operator fun BigDecimal.compareTo(other: BigInteger) = this.compareTo(other.toBigDecimal())

operator fun BigDecimal.compareTo(other: Int) = this.compareTo(other.toBigDecimal())

operator fun BigDecimal.compareTo(other: Long) = this.compareTo(other.toBigDecimal())

operator fun BigDecimal.compareTo(other: Short) = this.compareTo(other.toInt().toBigDecimal())

operator fun BigDecimal.compareTo(other: Byte) = this.compareTo(other.toInt().toBigDecimal())

operator fun BigDecimal.compareTo(other: Double) = this.compareTo(other.toBigDecimal())

operator fun BigDecimal.compareTo(other: Float) = this.compareTo(other.toBigDecimal())


/*
 BigInteger
 */

operator fun BigInteger.times(multiplier : Double) = this.toBigDecimal() * multiplier.toBigDecimal()

operator fun BigInteger.times(multiplier : Float) = this.toBigDecimal() * multiplier.toBigDecimal()

operator fun BigInteger.times(multiplier : Int) = this * multiplier.toBigInteger()

operator fun BigInteger.times(multiplier : Long) = this * multiplier.toBigInteger()

operator fun BigInteger.times(multiplier : Short) = this * multiplier.toInt().toBigInteger()

operator fun BigInteger.times(multiplier : Byte) = this * multiplier.toInt().toBigInteger()

operator fun BigInteger.plus(nr : BigDecimal) = this.toBigDecimal() + nr

operator fun BigInteger.plus(nr : Int) = this + nr.toBigInteger()

operator fun BigInteger.plus(nr : Long) = this + nr.toBigInteger()

operator fun BigInteger.plus(nr : Short) = this + nr.toInt().toBigInteger()

operator fun BigInteger.plus(nr : Byte) = this + nr.toInt().toBigInteger()

operator fun BigInteger.compareTo(other : BigDecimal) = this.toBigDecimal().compareTo(other)

operator fun BigInteger.compareTo(other : Int) = this.compareTo(other.toBigInteger())

operator fun BigInteger.compareTo(other : Long) = this.compareTo(other.toBigInteger())

operator fun BigInteger.compareTo(other : Short) = this.compareTo(other.toInt().toBigInteger())

operator fun BigInteger.compareTo(other : Byte) = this.compareTo(other.toInt().toBigInteger())

operator fun BigInteger.compareTo(other : Float) = this.compareTo(other.toBigDecimal())

operator fun BigInteger.compareTo(other : Double) = this.compareTo(other.toBigDecimal())
