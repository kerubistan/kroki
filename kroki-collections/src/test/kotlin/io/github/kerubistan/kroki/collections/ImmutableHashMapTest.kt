package io.github.kerubistan.kroki.collections

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ImmutableHashMapTest {
	@Test
	fun indexesForHash() {
		val hashTable = arrayOf(
			ImmutableHashMap.ImmutableHashMapEntry(1, "A"),
			ImmutableHashMap.ImmutableHashMapEntry(2, "B"),
			ImmutableHashMap.ImmutableHashMapEntry(3, "C"),
			ImmutableHashMap.ImmutableHashMapEntry(4, "D")
		)
		val indexesForHash = ImmutableHashMap.indexesForHash(1.hashCode(), hashTable)

		indexesForHash shouldBe 1 to 1
	}
}