package io.github.kerubistan.kroki.objects

import java.io.Closeable

/**
 * Same as the use function, but the receiver is the closeable.
 * Same as `use` and `apply` together, but way shorter.
 * Only syntax sugar.
 */
inline fun <T : Closeable?, R> T.useIt(block: T.() -> R): R =
	this.use {
		return@use this.block()
	}
