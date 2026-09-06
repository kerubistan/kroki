package io.github.kerubistan.kroki.collections

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ImmutableHashMapBuilderTest {

	@Test
	fun build() {

		val map = buildImmutableMap {
			put("A", "1")
			put("B", "2")
		}

		map shouldBe mapOf("A" to "1", "B" to "2")
		map.size shouldBe 2
		map.isEmpty() shouldBe false
		map.keys shouldBe setOf("A", "B")
		map.values shouldBe listOf("1", "2")
		map["A"] shouldBe "1"
		map["B"] shouldBe "2"
		map["C"] shouldBe null
		map.containsKey("A") shouldBe true
		map.containsKey("B") shouldBe true
		map.containsKey("C") shouldBe false
		map.containsValue("1") shouldBe true
		map.containsValue("2") shouldBe true
		map.containsValue("3") shouldBe false

		map.entries.size shouldBe 2
	}
}