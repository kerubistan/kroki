package io.github.kerubistan.kroki.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ComparablesKtTest {
	@Test
	fun comparator() {
		val input = listOf("foo", "bar", "baz")
		val ordered = listOf("bar", "baz", "foo")
		assertEquals(ordered, input.sortedWith(String::class.comparator()))
		assertEquals(input.sorted(), input.sortedWith(String::class.comparator()))
	}
}