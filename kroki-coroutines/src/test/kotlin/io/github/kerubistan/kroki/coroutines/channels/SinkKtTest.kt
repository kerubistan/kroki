package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class SinkKtTest {

	@Test
	fun sink() {
		runBlocking {
			val channel = produce {
				send("A")
				send("B")
			}
			sink(channel)
			while (channel.isEmpty) {
				delay(1)
			}
		}
	}
}