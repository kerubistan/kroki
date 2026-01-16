package io.github.kerubistan.kroki.time

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.test.assertEquals

class SafeDateFormatTest {

	fun buildDate(year: Int, month: Int, day: Int, hour: Int = 0, min: Int = 0, sec: Int = 0, millis: Int = 0) =
		GregorianCalendar().apply {
			set(Calendar.YEAR, year)
			set(Calendar.MONTH, month)
			set(Calendar.DAY_OF_MONTH, day)
			set(Calendar.HOUR_OF_DAY, hour)
			set(Calendar.MINUTE, min)
			set(Calendar.SECOND, sec)
			set(Calendar.MILLISECOND, millis)
		}.time

	@Test
	fun wrongPatterns() {
		assertThrows<IllegalArgumentException>("Week-Year with month and day") {
			SafeDateFormat("YYYYMMdd")
		}
	}

	@Test
	fun format() {
		assertEquals(
			"2017-01-27",
			SafeDateFormat("yyyy-MM-dd").format(buildDate(2017, 1, 27))
		)
		assertEquals(
			"2017-01-27 12:35",
			SafeDateFormat("yyyy-MM-dd hh:mm").format(buildDate(2017, 1, 27, 12, 35))
		)
		assertEquals(
			"2017-01-27 12:35:50",
			SafeDateFormat("yyyy-MM-dd hh:mm:ss").format(buildDate(2017, 1, 27, 12, 35, 50))
		)
	}
}