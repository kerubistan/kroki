package io.github.kerubistan.kroki.coroutines

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DelegatesKtTest {

	@Test
	fun eagerDelegate() {

		val original = (0 until 1000).map { "item $it" }.shuffled()
		val eagerOrdered by eager {
			original.sorted()
		}

		assertEquals(original.sorted(), eagerOrdered)
		assertEquals(original.sorted(), eagerOrdered)
	}
}