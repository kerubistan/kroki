package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ImmutableList2Test {
	@Test
	fun getSize() {
		assertEquals(2, immutableListOf("A", "B").size)
	}

	@Test
	fun isEmpty() {
		assertFalse { immutableListOf("A", "B").isEmpty() }
	}

	@Test
	fun contains() {
		assertTrue { immutableListOf("A", "B").contains("A") }
		assertTrue { immutableListOf("A", "B").contains("B") }
		assertFalse { immutableListOf("A", "B").contains("C") }
	}

	@Test
	fun iterator() {
		immutableListOf("A", "B").iterator().let { iterator ->
			assertTrue { iterator.hasNext() }
			assertTrue { iterator.hasNext() }
			assertEquals("A", iterator.next())
			assertTrue { iterator.hasNext() }
			assertEquals("B", iterator.next())
			assertFalse { iterator.hasNext() }
			assertThrows<IllegalArgumentException> { iterator.next() }
		}
	}

	@Test
	fun containsAll() {
		assertTrue { immutableListOf("A", "B").containsAll(immutableListOf("A", "B")) }
		assertTrue { immutableListOf("A", "B").containsAll(immutableListOf("A")) }
		assertTrue { immutableListOf("A", "B").containsAll(immutableListOf("B")) }
		assertFalse { immutableListOf("A", "B").containsAll(immutableListOf("A", "B", "C")) }
		assertFalse { immutableListOf("A", "B").containsAll(immutableListOf("C", "D")) }
	}

	@Test
	fun get() {
		assertEquals("A", immutableListOf("A", "B")[0])
		assertEquals("B", immutableListOf("A", "B")[1])
		assertThrows<IllegalArgumentException> { immutableListOf("A", "B")[2] }
		assertThrows<IllegalArgumentException> { immutableListOf("A", "B")[-1] }
	}

	@Test
	fun indexOf() {
		assertEquals(0, immutableListOf("A", "B").indexOf("A"))
		assertEquals(1, immutableListOf("A", "B").indexOf("B"))
		assertEquals(-1, immutableListOf("A", "B").indexOf("C"))
	}

	@Test
	fun lastIndexOf() {
		assertEquals(1, immutableListOf("A", "B").lastIndexOf("B"))
		assertEquals(0, immutableListOf("A", "B").lastIndexOf("A"))
		assertEquals(1, immutableListOf("A", "A").lastIndexOf("A"))
		assertEquals(-1, immutableListOf("A", "A").lastIndexOf("C"))
	}

	@Test
	fun listIterator() {
	}

	@Test
	fun testListIterator() {
	}

	@Test
	fun subList() {
		assertEquals(listOf("A"), immutableListOf("A", "B").subList(0, 1))
		assertEquals(listOf("B"), immutableListOf("A", "B").subList(1, 2))
		assertEquals(listOf<String>(), immutableListOf("A", "B").subList(1, 1))
		assertEquals(listOf<String>(), immutableListOf("A", "B").subList(0, 0))
	}

	@Test
	fun toStringTest() {
		assertEquals("[A,B]", immutableListOf("A", "B").toString())
	}

	@Test
	fun equals() {
		assertEquals(listOf("A", "B"), immutableListOf("A", "B"))
		assertNotEquals(listOf("A", "B", "C"), immutableListOf("A", "B"))
		assertNotEquals(listOf("B", "A"), immutableListOf("A", "B"))
	}

}