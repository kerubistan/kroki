package io.github.kerubistan.kroki.collections

import io.github.kerubistan.kroki.iteration.toList
import io.kotest.assertions.withClue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class ImmutableHashMapTest {
	@Test
	fun indexesForHash() {
		val hashTable = arrayOf(
			ImmutableMapEntry(1, "A"),
			ImmutableMapEntry(2, "B"),
			ImmutableMapEntry(3, "C1"),
			ImmutableMapEntry(3, "C2"),
			ImmutableMapEntry(3, "C3"),
			ImmutableMapEntry(4, "D")
		)

		withClue(1) {
			ImmutableHashMap.indexesForHash(1.hashCode(), hashTable) shouldBe (0 to 0)
		}
		withClue(2) {
			ImmutableHashMap.indexesForHash(2.hashCode(), hashTable) shouldBe (1 to 1)
		}
		withClue(3) {
			ImmutableHashMap.indexesForHash(3.hashCode(), hashTable) shouldBe (2 to 4)
		}
		withClue(10) {
			ImmutableHashMap.indexesForHash(10.hashCode(), hashTable) shouldBe null
		}
	}

	@Test
	fun get() {
		val map = immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		)

		map["A"] shouldBe "Alaska"
		map["B"] shouldBe "Belgium"
		map["C"] shouldBe "China"
		map["D"] shouldBe "Denmark"
		map["F"] shouldBe null
		map.size shouldBe 4
	}

	@Test
	fun keys() {
		immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		).keys shouldBe setOf("A", "B", "C", "D")

		immutableMapOf<String, String>().keys shouldBe setOf()
	}

	@Test
	fun values() {
		immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		).values shouldBe setOf("Alaska", "Belgium", "China", "Denmark")

		immutableMapOf<String, String>().values shouldBe setOf()
	}

	@Test
	fun entries() {
		immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		).entries.apply {
			this.size shouldBe 4
			this.isEmpty() shouldBe false
			this.iterator().apply {
				this shouldNotBe null
				this.toList().map { it.key }.toSet() shouldBe setOf("A", "B", "C", "D")
			}
		}

		immutableMapOf<String, String>().entries shouldBe setOf()
	}

	@Test
	fun containsKey() {
		immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		).apply {
			containsKey("A") shouldBe true
			containsKey("X") shouldBe false
		}
	}

	@Test
	fun equal() {
		immutableMapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		) shouldBeEqual mapOf(
			"A" to "Alaska",
			"B" to "Belgium",
			"C" to "China",
			"D" to "Denmark"
		)
	}
}