package io.github.kerubistan.kroki.coroutines.channels

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SortedListTest {

	@Test
	fun isEmpty() {
		val list = SortedList(listOf<Int>()) { first, second -> first.compareTo(second) }

		assertTrue(list.isEmpty())
		assertFalse(list.isNotEmpty())
	}

	@Test
	fun construct() {
		val list = SortedList(listOf(3, 1, 2)) { first, second -> first.compareTo(second) }
		assertFalse(list.isEmpty())
		assertEquals(3, list.removeFirst())
		assertFalse(list.isEmpty())
		assertEquals(2, list.removeFirst())
		assertFalse(list.isEmpty())
		assertEquals(1, list.removeFirst())
		assertTrue(list.isEmpty())
	}

	@Test()
	fun removeFirstEmptyList() {
		val list = SortedList(listOf<Int>()) { first, second -> first.compareTo(second) }
		assertThrows(IllegalStateException::class.java) {
			list.removeFirst()
		}
	}

	@Test
	fun add() {
		val list = SortedList(listOf<Int>()) { first, second -> first.compareTo(second) }

		list.add(2)
		list.add(1)
		list.add(3)

		assertEquals(3, list.removeFirst())
		assertEquals(2, list.removeFirst())
		assertEquals(1, list.removeFirst())
	}
}