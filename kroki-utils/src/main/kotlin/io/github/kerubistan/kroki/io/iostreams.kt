package io.github.kerubistan.kroki.io

import java.io.InputStream

fun InputStream.peek(process: (InputStream) -> Unit): InputStream {
	return if(markSupported()) {
		mark(0)
		process(this)
		reset()
		this
	} else {
		val fork = ForkInputStream(this)
		process(fork)
		ConcatenatedInputStream(listOf(fork.buffer.toByteArray().inputStream(), this))
	}
}
