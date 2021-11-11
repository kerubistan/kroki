package io.github.kerubistan.kroki.io

import java.io.InputStream

fun InputStream.peek(process: (InputStream) -> Unit): InputStream {
	val fork = ForkInputStream(this)
	process(fork)
	return ConcatenatedInputStream(listOf(fork.buffer.toByteArray().inputStream(), this))
}
