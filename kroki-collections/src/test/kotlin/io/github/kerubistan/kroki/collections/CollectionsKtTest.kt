package io.github.kerubistan.kroki.collections

import io.kotest.matchers.collections.*
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
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

	@Test
	fun buildList() {
		buildList<String> { } shouldBe emptyList()
		buildList<String> { add("A") } shouldBe listOf("A")
		buildList<String> {
			add("A")
			add("B")
			add("C")
		} shouldBe listOf("A", "B", "C")
		buildList<String> { repeat(1000) { add(it.toString()) } }.let {
			it shouldStartWith "0"
			it shouldEndWith "999"
			it shouldHaveSize 1000
		}
	}
}