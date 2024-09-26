package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ImmutableArrayListTest {

	@Test
	fun equals() {
		assertEquals(immutableListOf("A", "B", "C"), immutableListOf("A", "B", "C"))
		assertEquals(immutableListOf("A", "B", "C"), listOf("A", "B", "C"))
		assertNotEquals(immutableListOf("A", "B", "C"), listOf("A", "B", "C", "D"))
		assertNotEquals(immutableListOf("A", "B", "C", "D"), listOf("A", "B", "C"))
		assertNotEquals(immutableListOf("A", "B", "C"), listOf("A", "D", "C"))
		assertNotEquals(immutableListOf("A", "B", "C"), setOf("A", "D", "C"))
	}

	@Test
	fun size() {
		assertEquals(2, ImmutableArrayList(arrayOf("A", "B")).size)
	}

	@Test
	fun isEmpty() {
		assertFalse {
			ImmutableArrayList(arrayOf("A", "B")).isEmpty()
		}
	}

	@Test
	fun indexOf() {
		assertEquals(1, ImmutableArrayList(arrayOf("A", "B", "C")).indexOf("B"))
		assertEquals(-1, ImmutableArrayList(arrayOf("A", "B", "C")).indexOf("D"))
	}

	@Test
	fun iterator() {
		val iterator = ImmutableArrayList(arrayOf("A", "B", "C")).iterator()
		assertTrue { iterator.hasNext() }
		assertEquals("A", iterator.next())
		assertTrue { iterator.hasNext() }
		assertEquals("B", iterator.next())
		assertTrue { iterator.hasNext() }
		assertEquals("C", iterator.next())
		assertFalse { iterator.hasNext() }
	}

	@Test
	fun listIterator() {
		val listIterator = ImmutableArrayList(arrayOf("A", "B", "C")).listIterator()
		assertTrue { listIterator.hasNext() }
		assertFalse { listIterator.hasPrevious() }
		assertEquals("A", listIterator.next())
		assertTrue { listIterator.hasNext() }
		assertTrue { listIterator.hasPrevious() }
		assertEquals("A", listIterator.previous())
		assertTrue { listIterator.hasNext() }
		assertFalse { listIterator.hasPrevious() }
	}

	@Test
	fun listIteratorWithIndex() {
		val list = immutableListOf("A", "B", "C", "D")
		val iterator = list.listIterator(1)
		assertTrue(iterator.hasNext())
		assertEquals("B", iterator.next())
		assertTrue(iterator.hasNext())
		assertEquals("C", iterator.next())
		assertTrue(iterator.hasNext())
		assertEquals("D", iterator.next())
		assertFalse (iterator.hasNext())
		assertThrows<ArrayIndexOutOfBoundsException> { iterator.next() }
	}

	@Test
	fun subList() {
		assertEquals(emptyList(), immutableListOf(1, 2, 3).subList(0, 0))
		// more in the sublist tests
	}

	@Test
	fun testToString() {
		assertEquals("[A,B,C]", ImmutableArrayList(arrayOf("A", "B", "C")).toString())
	}

	@Test
	fun contains() {
		assertTrue { ImmutableArrayList(arrayOf("A", "B", "C")).contains("C") }
		assertFalse { ImmutableArrayList(arrayOf("A", "B", "C")).contains("D") }
	}

	@Test
	fun containsAll() {
		assertTrue { ImmutableArrayList(arrayOf("A", "B", "C")).containsAll(listOf("A", "B", "C")) }
		assertTrue { ImmutableArrayList(arrayOf("A", "B", "C")).containsAll(listOf("A", "B")) }
		assertFalse { ImmutableArrayList(arrayOf("A", "B", "C")).containsAll(listOf("A", "B", "C", "D")) }
		assertTrue { ImmutableArrayList(arrayOf("A", "B", "C")).containsAll(listOf()) }
	}

	@Test
	fun lastIndexOf() {
		assertEquals(-1, ImmutableArrayList.of("A", "B", "A").lastIndexOf("C"))
		assertEquals(2, ImmutableArrayList.of("A", "B", "A").lastIndexOf("A"))
	}

	@Test
	fun get() {
		assertEquals("A", ImmutableArrayList.of("A", "B", "C")[0])
		assertEquals("B", ImmutableArrayList.of("A", "B", "C")[1])
		assertThrows<ArrayIndexOutOfBoundsException> {
			ImmutableArrayList.of("A", "B", "C")[3]
		}
	}

	@Test
	fun testEquals() {
		assertEquals(immutableListOf(1,2,3), immutableListOf(1,2,3))
		assertEquals(immutableListOf(1,2,3), listOf(1,2,3))
		assertEquals(immutableListOf(1,2,3), arrayListOf(1,2,3))

		assertNotEquals(immutableListOf(1,2,3), setOf(1,2,3))
		assertNotEquals(immutableListOf(1,2,3), emptyList<Int>())
		assertNotEquals(immutableListOf(1,2,3), "")
	}

	@Test
	fun testHashCode() {
		// when two objects are equal, their hashcode should also be equal
		assertEquals(immutableListOf("A", "B").hashCode(), ArrayList(listOf("A", "B")).hashCode())
		assertEquals(immutableListOf("A", "B").hashCode(), arrayListOf("A", "B").hashCode())
	}
}
