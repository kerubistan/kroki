package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapsKtTest {

	@Test
	fun upsert() {
		assertEquals(
			mapOf("A" to 1, "B" to 2),
			mapOf("A" to 1).upsert("B", init = { 2 }, mapper = { 3 /*not called in this case*/ })
		)
		assertEquals(
			mapOf("A" to 1, "B" to 2),
			mapOf("A" to 1, "B" to 1).upsert("B", init = { 3 /*not called in this case*/ }, mapper = { it + 1 })
		)
	}

	@Test
	fun mapUpdate() {
		assertEquals(
			mapOf(
				1 to "one",
				2 to "two",
				3 to "three"
			),
			mapOf(
				1 to "one",
				2 to "two",
				3 to "drei"
			).update(3) { "three" }
		)

		// we are not adding, only updating
		assertEquals(
			mapOf(
				1 to "one",
				2 to "two",
				3 to "three"
			),
			mapOf(
				1 to "one",
				2 to "two",
				3 to "three"
			).update(4) { "four" }
		)
	}

	@Test
	fun filterNotNullValues() {
		assertEquals(
			mapOf(
				"A" to "a",
				"B" to "b"
			),
			mapOf(
				"A" to "a",
				"X" to null,
				"B" to "b"
			).filterNotNullValues()
		)
	}


}