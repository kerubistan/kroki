package io.github.kerubistan.kroki.coroutines.channels.fs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.io.File
import java.nio.charset.Charset

fun CoroutineScope.readLines(
	file: File,
	charset: Charset = Charsets.UTF_8
): ReceiveChannel<String> = this.readLines(file, charset) { it }

inline fun <T> CoroutineScope.readLines(
	file: File,
	charset: Charset = Charsets.UTF_8,
	crossinline transform: (String) -> T
): ReceiveChannel<T> = produce {
	val bufferedReader = file.reader(charset).buffered()
	try {
		var line: String? = bufferedReader.readLine()
		while (line != null) {
			send(transform(line))
			line = bufferedReader.readLine()
		}
	} finally {
		bufferedReader.close()
	}
}