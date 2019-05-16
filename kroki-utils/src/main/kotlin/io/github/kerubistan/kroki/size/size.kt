package io.github.kerubistan.kroki.size

import java.math.BigInteger

private val K = 1024.toBigInteger()

val Int.KB: BigInteger
    get() = this.toBigInteger() * K

val Int.MB: BigInteger
    get() = this.KB * K

val Int.GB: BigInteger
    get() = this.MB * K

val Int.TB: BigInteger
    get() = this.GB * K

val Int.PB: BigInteger
    get() = this.TB * K
