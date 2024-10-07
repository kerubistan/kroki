package io.github.kerubistan.kroki.collections

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldEndWith
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContainAll
import io.kotest.matchers.collections.shouldStartWith
import io.kotest.matchers.should
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CollectionsKtTest {

	@Test
	fun testImmutableListOf() {
		immutableListOf(1).shouldNotBeEmpty()
		immutableListOf(1).shouldBeSingleton()
		immutableListOf<Int>().shouldBeEmpty()
		immutableListOf(1, 2, 3).shouldStartWith(1)
		immutableListOf(1, 2, 3).shouldEndWith(3)
		immutableListOf(1, 2, 3).shouldContainAll(1, 2)
		immutableListOf(1, 2, 3).shouldContainAll(1, 2, 3)
		immutableListOf(1, 2, 3).shouldNotContainAll(1, 2, 3, 4)

		assertEquals(listOf<String>(), listOf<String>().toImmutableList())
		assertEquals(listOf("A"), listOf("A").toImmutableList())
		assertEquals(listOf("A", "B"), listOf("A", "B").toImmutableList())
	}
}