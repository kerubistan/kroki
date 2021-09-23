package io.github.kerubistan.kroki.exceptions

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class ExceptionsKtTest {

	@Test
	fun getStackTraceAsString() {
		val stackTrace = IllegalArgumentException("TEST").getStackTraceAsString()
		assertTrue(stackTrace.contains("TEST"))
	}

	@Test
	fun insistTest() {
		var errors = 0
		val result = insist(tries = 3, onError = { _, _ -> errors++ }) {
			if (errors < 2) {
				throw IllegalStateException("testing")
			} else {
				"PASS"
			}
		}

		assertEquals("PASS", result)
		assertEquals(2, errors)
	}

	@Test
	fun insistTestAndFail() {
		var errors = 0
		assertThrows<IllegalStateException> {
			insist(tries = 1, onError = { attempt, _ ->  errors = attempt }) {
				if (errors < 2) {
					throw IllegalStateException("testing")
				} else {
					"WON'T HAPPEN"
				}
			}
		}

	}

}