package io.github.kerubistan.kroki.collections

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ArrayTransformIteratorTest {

	@Test
	operator fun next() {
		ArrayTransformIterator(arrayOf("A", "B", "C")) {
			it.lowercase()
		}.let {
			it.hasNext() shouldBe true
			it.hasPrevious() shouldBe false
			it.nextIndex() shouldBe 1
			it.next() shouldBe "a"
			it.nextIndex() shouldBe 2
			it.previousIndex() shouldBe 0
			it.hasNext() shouldBe true
			it.hasPrevious() shouldBe true
			it.next() shouldBe "b"
			it.nextIndex() shouldBe 3
			it.previousIndex() shouldBe 1
			it.hasNext() shouldBe true
			it.hasPrevious() shouldBe true
			it.next() shouldBe "c"
			it.hasNext() shouldBe false
			it.hasPrevious() shouldBe true
			org.junit.jupiter.api.assertThrows<IllegalStateException> {
				it.next()
			}
		}
	}
}