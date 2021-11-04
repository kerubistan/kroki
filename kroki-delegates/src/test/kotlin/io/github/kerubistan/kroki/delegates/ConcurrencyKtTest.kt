package io.github.kerubistan.kroki.delegates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.Thread.yield
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.test.assertNull

internal class ConcurrencyKtTest {

	@Test
	fun threadLocalTest() {
		// GIVEN
		val dateStr = "1969-07-16 13:32:00"
		val dateFormat by threadLocal { SimpleDateFormat("yyyy-MM-dd hh:mm:ss") }
		val results = mutableSetOf<Date>()

		// WHEN
		(1..1024).map {
			thread {
				val dates = (1..1024).map {
					yield()
					dateFormat.parse(dateStr)
				}.toSet()
				synchronized(results) {
					results.addAll(dates)
				}
			}
		}.map { it.join() }

		//THEN
		assertEquals(1, results.size)
	}

	@Test
	fun assignableThreadLocal() {
		var x by threadLocal<String>()
		assertNull(x)
		x = "AAAA"
		assertEquals("AAAA", x)
		thread {
			assertNull(x)
			x = "BBBB"
			assertEquals("BBBB", x)
		}.join()
		assertEquals("AAAA", x)
	}

	@Test
	fun assignableThreadLocalWithInitialValue() {
		var x by threadLocal("initial value")
		assertEquals("initial value", x)
		thread {
			assertEquals("initial value", x)
		}.join()
		x = "updated value"
		assertEquals("updated value", x)
		thread {
			assertEquals("initial value", x)
		}.join()
	}

}