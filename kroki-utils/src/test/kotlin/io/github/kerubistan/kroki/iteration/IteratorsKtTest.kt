package io.github.kerubistan.kroki.iteration

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class IteratorsKtTest {

	@Test
	fun map() {
		assertEquals(
			listOf(2, 3, 4, 5, 6),
			listOf(1, 2, 3, 4, 5).iterator().map { it + 1 }.toList()
		)
	}

	@Test
	fun toList() {
		assertEquals(listOf(1, 2, 3, 4), listOf(1, 2, 3, 4).iterator().toList())
	}


}