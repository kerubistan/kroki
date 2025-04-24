package io.github.kerubistan.kroki.collections

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ImmutableSingletonMapTest {

	@Test
	fun getEntries() {
		immutableMapOf(
			"A" to "Alaska"
		).entries.apply {
			size shouldBe 1
		}
	}

	@Test
	fun getKeys() {
		immutableMapOf(
			"A" to "Alaska"
		).keys shouldBe setOf("A")
	}

	@Test
	fun getSize() {
		immutableMapOf(
			"A" to "Alaska"
		).size shouldBe 1
	}

	@Test
	fun getValues() {
		immutableMapOf(
			"A" to "Alaska"
		).values shouldBe setOf("Alaska")
	}

	@Test
	fun isEmpty() {
		immutableMapOf(
			"A" to "Alaska"
		).isEmpty() shouldBe false
	}

	@Test
	fun get() {
		immutableMapOf(
			"A" to "Alaska"
		).apply {
			this["A"] shouldBe "Alaska"
			this["X"] shouldBe null
		}
	}

	@Test
	fun containsValue() {
		immutableMapOf(
			"A" to "Alaska"
		).apply {
			this.containsValue("Alaska") shouldBe true
			this.containsValue("X") shouldBe false
		}
	}

	@Test
	fun containsKey() {
		immutableMapOf(
			"A" to "Alaska"
		).apply {
			this.containsKey("A") shouldBe true
			this.containsKey("X") shouldBe false
		}
	}
}