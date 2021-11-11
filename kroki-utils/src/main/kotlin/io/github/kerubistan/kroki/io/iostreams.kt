package io.github.kerubistan.kroki.io

import java.io.BufferedInputStream
import java.io.InputStream

fun InputStream.peek(process: (InputStream) -> Unit): InputStream {
	return if (markSupported()) {
		mark(0)
		process(this)
		reset()
		this
	} else {
		BufferedInputStream(this).let {
			it.mark(0)
			process (it)
			it.reset ()
			it
		}
	}
}
