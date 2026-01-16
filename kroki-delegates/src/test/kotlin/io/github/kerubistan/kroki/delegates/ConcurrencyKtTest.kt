package io.github.kerubistan.kroki.delegates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.Thread.yield
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.thread
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ConcurrencyKtTest {

	@Test
	fun threadLocalTest() {
		// GIVEN
		val dateStr = "1969-07-16 13:32:00"
		val pattern = "yyyy-MM-dd hh:mm:ss"
		// not here that SimpleDateFormat is famous about not being thread-safe
		// if you call it from several parallel threads, it will sometimes calculate
		// wrong result
		// by making it thread-local, we can make it safe and still fast
		val dateFormat by threadLocal { SimpleDateFormat(pattern) }
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
		assertTrue { results.all { it == SimpleDateFormat(pattern).parse(dateStr) } }
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