package io.github.kerubistan.kroki.io

import java.io.BufferedInputStream
import java.io.InputStream

/**
 * Allows the program to read from the input in a function literal, make decisions
 * and later still be able to read the whole input.
 *
 * @receiver the InputStream you want to peek into
 * @param process the code that reads some of the input
 * @return an input stream that is in the state like before the process peeked
 * @sample io.github.kerubistan.kroki.io.IostreamsKtTest.peekSample
 */
fun InputStream.peek(process: (InputStream) -> Unit): InputStream {
	return if (markSupported()) {
		mark(0)
		process(this)
		reset()
		this
	} else {
		BufferedInputStream(this).let {
			it.mark(0)
			process(it)
			it.reset()
			it
		}
	}
}
