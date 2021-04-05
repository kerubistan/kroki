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

		var counter = 0
		val data by cached { ++counter }

		assertEquals(1, data)
		assertEquals(1, data)
		assertEquals(1, counter)

	}

	@Test
	fun cachedDelegateErrorHandling() {

		val errors = mutableListOf<Throwable>()
		var counter = 0
		val data by cached(retryOnFail = 10, errorHandler = { errors.add(it) }) {
			if (counter++ > 2)
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

		var counter = 0
		val data by cached(retryOnFail = 1) {
			if (counter++ > 2)
				1
			else throw UnknownHostException("blah.example.com not known")
		}

		assertThrows<UnknownHostException> { println(data) }

	}


}