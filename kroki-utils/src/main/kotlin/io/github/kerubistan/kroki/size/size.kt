package io.github.kerubistan.kroki.size

import java.math.BigInteger

private val K = 1024.toBigInteger()

/**
 * A human-friendly way to express size in kilobytes.
 * @sample io.github.kerubistan.kroki.size.SizeKtTest.checkSizes
 */
val Int.KB: BigInteger
	get() = this.toBigInteger() * K

/**
 * A human-friendly way to express size in megabytes.
 * @sample io.github.kerubistan.kroki.size.SizeKtTest.checkSizes
 */
val Int.MB: BigInteger
	get() = this.KB * K

/**
 * A human-friendly way to express size in gigabytes.
 * @sample io.github.kerubistan.kroki.size.SizeKtTest.checkSizes
 */
val Int.GB: BigInteger
	get() = this.MB * K

/**
 * A human-friendly way to express size in terra-bytes.
 * @sample io.github.kerubistan.kroki.size.SizeKtTest.checkSizes
 */
val Int.TB: BigInteger
	get() = this.GB * K

/**
 * A human-friendly way to express size in petabytes.
 * @sample io.github.kerubistan.kroki.size.SizeKtTest.checkSizes
 */
val Int.PB: BigInteger
	get() = this.TB * K
