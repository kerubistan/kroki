package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FilterKtTest {

	@Test
	fun filter() {
		runBlocking {
			val filtered = filter(produce {
				(1..10).forEach { i ->
					this.send(i)
				}
			}) {
				it % 2 == 0
			}
			val results = mutableListOf<Int>()
			for(message in filtered) {
				results.add(message)
			}
			assertEquals(listOf(2,4,6,8,10), results)
		}
	}
}