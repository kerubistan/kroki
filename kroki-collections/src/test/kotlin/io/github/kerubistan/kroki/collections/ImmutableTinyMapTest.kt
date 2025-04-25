package io.github.kerubistan.kroki.collections

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ImmutableTinyMapTest {
	@Test
	fun behavior() {
		val map = immutableMapOf(1 to "A", 2 to "B")

		map.size shouldBe 2
		map.isEmpty() shouldBe false
		map.keys shouldBe setOf(1, 2)
		map.values shouldBe listOf("A", "B")
		map[1] shouldBe "A"
		map[2] shouldBe "B"
		map.containsKey(1) shouldBe true
		map.containsKey(2) shouldBe true
		map.containsKey(3) shouldBe false
		map.containsValue("A") shouldBe true
		map.containsValue("B") shouldBe true
		map.containsValue("C") shouldBe false
		map shouldBe mapOf(1 to "A", 2 to "B")
	}
}