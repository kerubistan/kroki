package io.github.kerubistan.kroki.time

/**
 * UTC time from the system expressed in a short and human-readable way.
 */
fun now() = System.currentTimeMillis()

// considered temporary solution for readability while kotlin.time is experimental

val Int.H: Long
	get() = this * 60.MIN

val Int.MIN: Long
	get() = this * 60.SEC

val Int.SEC: Long
	get() = this * 1000.MS

val Int.MS: Long
	get() {
		require(this >= 0) { " Duration must be positive. Given: $this" }
		return this.toLong()
	}

val Int.D: Long
	get() = this * 24.H

val Int.W: Long
	get() = this * 7.D
