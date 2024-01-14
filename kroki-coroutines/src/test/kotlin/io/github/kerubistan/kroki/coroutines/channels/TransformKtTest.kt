package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TransformKtTest {

	@Test
	fun transform() {
		runBlocking {
			val transformed = transform(produce {
				send("A")
				send("B")
				send("C")
			}) {
				it.lowercase()
			}
			val results = mutableListOf<String>()
			for (message in transformed) {
				results.add(message)
			}
			assertEquals(listOf("a", "b", "c"), results)
		}
	}

	@Test
	fun transformNotNull() {
		runBlocking {
			val transformed = transformNotNull(produce {
				send("A")
				send("AA")
				send("B")
				send("C")
			}) {
				if (it.length == 1) {
					it.lowercase()
				} else null
			}
			val results = mutableListOf<String>()
			for (message in transformed) {
				results.add(message)
			}
			assertEquals(listOf("a", "b", "c"), results)
		}
	}
}