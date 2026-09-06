package io.github.kerubistan.kroki.collections

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.collections.shouldNotContainAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class ArrayTransformListTest {

	@Test
	fun getSize() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).size shouldBe 3
		ArrayTransformList(arrayOf(), Int::toString).size shouldBe 0
	}

	@Test
	fun get() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).let {
			it[0] shouldBe "1"
			it[1] shouldBe "2"
			it[2] shouldBe "3"
			assertThrows<IndexOutOfBoundsException> { it[3] }
			assertThrows<IndexOutOfBoundsException> { it[-1] }
		}
	}

	@Test
	fun isEmpty() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).isEmpty() shouldBe false
		ArrayTransformList(arrayOf(), Int::toString).isEmpty() shouldBe true
	}

	@Test
	fun listIterator() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).listIterator().let {
			it.hasPrevious() shouldBe false
			it.hasNext() shouldBe true
			it.next() shouldBe "1"
			it.hasPrevious() shouldBe true
			it.hasNext() shouldBe true
			it.next() shouldBe "2"
			it.hasPrevious() shouldBe true
			it.hasNext() shouldBe true
			it.next() shouldBe "3"
			it.hasPrevious() shouldBe true
			it.hasNext() shouldBe false
		}
	}

	@Test
	fun subList() {
		ArrayTransformList(arrayOf(0, 1, 2, 3, 4), Int::toString).let {
			it.subList(0, 2) shouldBe listOf("0", "1")
		}
	}

	@Test
	fun lastIndexOf() {
		ArrayTransformList(arrayOf(1, 2, 3, 3), Int::toString).let {
			it.lastIndexOf("1") shouldBe 0
			it.lastIndexOf("3") shouldBe 3
		}
	}

	@Test
	fun indexOf() {
		ArrayTransformList(arrayOf(1, 2, 3, 3), Int::toString).let {
			it.indexOf("1") shouldBe 0
			it.indexOf("3") shouldBe 2
		}
	}

	@Test
	fun containsAll() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).let {
			it shouldContainAll listOf()
			it shouldContainAll listOf("1", "2")
			it shouldContainAll listOf("1", "2", "3")
			it shouldNotContainAll listOf("1", "2", "3", "4")
		}
	}

	@Test
	fun contains() {
		ArrayTransformList(arrayOf(1, 2, 3), Int::toString).let {
			it shouldContain "1"
			it shouldContain "2"
			it shouldContain "3"
			it shouldNotContain "4"
			it shouldNotContain "0"
		}
		ArrayTransformList(arrayOf(), Int::toString).let {
			it shouldNotContain "1"
			it shouldNotContain "0"
		}
	}
}