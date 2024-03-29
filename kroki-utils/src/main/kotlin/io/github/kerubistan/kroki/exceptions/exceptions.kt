package io.github.kerubistan.kroki.exceptions

import io.github.kerubistan.kroki.io.EasyStringWriter
import java.io.PrintWriter

/**
 * The stack trace of the throwable as string.
 */
val Throwable.stackTraceText: String
	get() = EasyStringWriter(256).use { stringWriter ->
		PrintWriter(stringWriter).use {
			this.printStackTrace(it)
		}
		stringWriter.toString()
	}

@Deprecated(
	replaceWith = ReplaceWith("stackTraceText", "io.github.kerubistan.kroki.exceptions.stackTraceText"),
	message = "stackTraceText reads way better",
	level = DeprecationLevel.WARNING
)
fun Throwable.getStackTraceAsString(): String = this.stackTraceText

/**
 * Sometimes, typically with remote calls things can go bad, and it is OK
 * to try again. Such cases are like loading an RSS feed,
 * @param tries the maximum number of tries
 * @param action the action to perform
 * @param onError action taken on error
 * @param T return type
 * @sample io.github.kerubistan.kroki.exceptions.ExceptionsKtTest.insistTest
 * @sample io.github.kerubistan.kroki.exceptions.ExceptionsKtTest.insistTestAndFail
 */
inline fun <T> insist(tries: Int, onError: (attempt: Int, t: Throwable) -> Unit, action: () -> T): T {
	for (attempt in 0 until tries) {
		try {
			return action()
		} catch (t: Throwable) {
			onError(attempt, t)
		}
	}
	return action()
}
