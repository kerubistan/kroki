package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShuffleKtTest {

	@Test
	fun shuffle() = runBlocking {
		val results = mutableListOf<Int>()
		for (message in shuffle(
			produce { (1..100).forEach { send(it) } },
			capacity = 10
		)) {
			results.add(message)
		}

		assertEquals((1..100).toList(), results.sorted())
	}
}