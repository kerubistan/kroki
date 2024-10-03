package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CollectionsKtTest {

	@Test
	fun immutableListOf() {
		assertEquals(listOf<String>(), listOf<String>().toImmutableList())
		assertEquals(listOf("A"), listOf("A").toImmutableList())
		assertEquals(listOf("A", "B"), listOf("A", "B").toImmutableList())
	}
}