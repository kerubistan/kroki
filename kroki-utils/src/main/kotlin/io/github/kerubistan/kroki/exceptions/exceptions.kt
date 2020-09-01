package io.github.kerubistan.kroki.exceptions

import io.github.kerubistan.kroki.io.EasyStringWriter
import java.io.PrintWriter

fun Throwable.getStackTraceAsString(): String =
	EasyStringWriter(256).use { stringWriter ->
		PrintWriter(stringWriter).use {
			this.printStackTrace(it)
		}
		stringWriter.toString()
	}

/**
 * Sometimes, typically with remote calls things can go bad, and it is OK
 * to try again. Such cases are like loading an RSS feed,
 */
inline fun <T> insist(tries: Int, onError : (t: Throwable) -> Unit, action: () -> T): T {
	for (attempt in 0 until tries) {
		try {
			return action()
		} catch (t: Throwable) {
			onError(t)
		}
	}
	return action()
}
