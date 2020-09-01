package io.github.kerubistan.kroki.coroutines

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException
import kotlin.test.assertTrue

internal class DelegatesKtTest {

	@Test
	fun eagerDelegate() {

		val original = (0 until 1000).map { "item $it" }.shuffled()
		val eagerOrdered by eager {
			original.sorted()
		}

		assertEquals(original.sorted(), eagerOrdered)
		assertEquals(original.sorted(), eagerOrdered)
	}

	@Test
	fun cachedDelegate() {

		var cntr = 0
		val data by cached { ++cntr }

		assertEquals(1, data)
		assertEquals(1, data)
		assertEquals(1, cntr)

	}

	@Test
	fun cachedDelegateErrorHandling() {

		val errors = mutableListOf<Throwable>()
		var cntr = 0
		val data by cached(retryOnFail = 10, errorHandler = { errors.add(it) }) {
			if (cntr++ > 2)
				1
			else throw UnknownHostException("blah.example.com not known")
		}

		assertEquals(1, data)
		assertEquals(1, data)
		assertEquals(3, errors.size)
		assertTrue(errors.all { it is UnknownHostException })

	}

	@Test
	fun cachedDelegateErrorHandlingFail() {

		var cntr = 0
		val data by cached(retryOnFail = 1) {
			if (cntr++ > 2)
				1
			else throw UnknownHostException("blah.example.com not known")
		}

		assertThrows<UnknownHostException> { println(data) }

	}


}