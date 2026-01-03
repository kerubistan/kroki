package io.github.kerubistan.kroki.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ComparablesKtTest {
	@Test
	fun comparator() {
		val input = listOf("foo", "bar", "baz")
		val ordered = listOf("bar", "baz", "foo")
		assertEquals(ordered, input.sortedWith(String::class.comparator()))
		assertEquals(input.sorted(), input.sortedWith(String::class.comparator()))
	}

	@Test
	fun isGreaterThan() {
		assertTrue(1.isGreaterThan(0) { a, b -> a.compareTo(b) })
	}

	@Test
	fun isLessThan() {
		assertTrue(2.isLessThan(3) { a, b -> a.compareTo(b) })
	}
}