package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class ImmutableSubArrayListTest {

	@Test
	fun validations() {
		assertThrows<IllegalArgumentException> {
			immutableListOf(1, 2, 3).subList(-1, 2)
		}
		assertThrows<IllegalArgumentException> {
			immutableListOf(1, 2, 3).subList(1, 5)
		}
		assertThrows<IllegalArgumentException> {
			immutableListOf(1, 2, 3).subList(1, 0)
		}
	}

	@Test
	fun getSize() {
		assertEquals(1, immutableListOf(1,2,3).subList(0,1).size)
		// this is not even done by a sublist, just emptyList
		assertEquals(0, immutableListOf(1,2,3).subList(0,0).size)
	}

	@Test
	fun get() {
		kotlin.test.assertEquals(0, immutableListOf(0, 1, 2).subList(0, 1)[0])
		kotlin.test.assertEquals(1, immutableListOf(0, 1, 2).subList(0, 2)[1])
	}

	@Test
	fun isEmpty() {
	}

	@Test
	operator fun iterator() {
	}

	@Test
	fun listIterator() {
	}

	@Test
	fun testListIterator() {
	}

	@Test
	fun subList() {
	}

	@Test
	fun lastIndexOf() {
	}

	@Test
	fun indexOf() {
	}

	@Test
	fun containsAll() {
		assertTrue { immutableListOf(0,1,2,3,4).subList(1,3).containsAll(setOf(1,2)) }
	}

	@Test
	fun contains() {
		assertTrue { immutableListOf(0,1,2,3,4).subList(0,2).contains(0) }
		assertTrue { immutableListOf(0,1,2,3,4).subList(0,2).contains(1) }
		assertTrue { immutableListOf(0,1,2,3,4).subList(1,3).contains(1) }
	}

	@Test
	fun testToString() {
		assertEquals("[A,B,C]", immutableListOf("A", "B", "C").subList(0,3).toString())
	}

}