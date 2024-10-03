package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
		assertFalse(immutableListOf("A", "B", "C").subList(1, 3).isEmpty())
		assertTrue(immutableListOf("A", "B", "C").subList(3, 3).isEmpty())
	}

	@Test
	operator fun iterator() {
		immutableListOf(0,1,2,3).subList(1,4).iterator().let {
			assertTrue(it.hasNext())
			assertEquals(1, it.next())
			assertTrue(it.hasNext())
			assertEquals(2, it.next())
			assertTrue(it.hasNext())
			assertEquals(3, it.next())

			assertFalse(it.hasNext())
			assertThrows<IllegalArgumentException> { it.next() }
		}
	}

	@Test
	fun listIterator() {
		immutableListOf(0,1,2,3).subList(1,4).listIterator().let {
			assertFalse(it.hasPrevious())
			assertThrows<IllegalArgumentException> { it.previous() }
			assertTrue(it.hasNext())
			assertEquals(1, it.next())
			assertTrue(it.hasPrevious())
			assertTrue(it.hasNext())
		}
	}

	@Test
	fun testListIterator() {
	}

	@Test
	fun subList() {
		assertEquals(
			listOf("C", "D"),
			immutableListOf("A", "B", "C", "D").subList(1, 4).subList(1, 3)
		)
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

	@Test
	fun testHashCode() {
		assertEquals(
			listOf(1,2,3,4).hashCode(),
			immutableListOf(0,1,2,3,4).subList(1,5).hashCode()
		)
	}

	@Test
	fun testEquals() {
		assertTrue(immutableListOf(0,1,2,3).subList(0,0) == emptyList<Int>())
		assertTrue(immutableListOf(0,1,2,3).subList(0,1) == listOf(0))
	}

}