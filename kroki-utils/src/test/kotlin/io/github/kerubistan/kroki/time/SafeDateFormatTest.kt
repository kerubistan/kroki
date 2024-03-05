package io.github.kerubistan.kroki.time

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class SafeDateFormatTest {

	@Test
	fun wrongPatterns() {
		assertThrows<IllegalArgumentException>("Week-Year with month and day") {
			SafeDateFormat("YYYYMMdd")
		}
	}

	@Test
	fun format() {
		assertEquals("2017-01-27",
		SafeDateFormat("yyyy-MM-dd").format(GregorianCalendar().apply {
			set(Calendar.YEAR, 2017)
			set(Calendar.MONTH, 1)
			set(Calendar.DAY_OF_MONTH, 27)
		}.time))
	}
}