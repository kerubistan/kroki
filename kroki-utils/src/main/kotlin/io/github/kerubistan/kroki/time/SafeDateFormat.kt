package io.github.kerubistan.kroki.time

import java.time.LocalDate
import java.util.*

/**
 *
 */
class SafeDateFormat(private val pattern: String, private val timeZone: TimeZone = TimeZone.getDefault()) {

	private val compiledPattern: Array<DatePatternElement> = compilePattern(pattern)

	companion object {
		private val factoryMap = mapOf(
			'Y' to { _: Int -> throw IllegalArgumentException("week-year is not supported") },
			'y' to { length: Int -> Year(length) },
			'M' to { length: Int -> Month(length) },
			'd' to { length: Int -> Day(length) },
			'h' to { length: Int -> Hour(length) },
			'm' to { length: Int -> Minute(length) },
			's' to { length: Int -> Second(length) },
			'S' to { length: Int -> MilliSeconds(length) },
			'Z' to { length: Int -> TimeZoneOffset }
		)

		internal fun compilePattern(pattern: String): Array<DatePatternElement> {
			val groups = mutableListOf<StringBuilder>()
			pattern.toCharArray().forEach { char ->
				if (groups.isNotEmpty()) {
					if (char in factoryMap.keys) {
						if (groups.last().startsWith(char)) {
							groups.last().append(char)
						} else {
							groups.add(StringBuilder().append(char))
						}
					} else {
						if (groups.last().first() !in factoryMap.keys) {
							groups.last().append(char)
						} else {
							groups.add(StringBuilder().append(char))
						}
					}
				} else {
					groups.add(StringBuilder().append(char))
				}
			}
			return groups.map { pattern ->
				val key = pattern.first()
				if (key in factoryMap.keys) {
					factoryMap.get(key)!!.invoke(pattern.length)
				} else {
					Text(pattern.toString())
				}
			}.toTypedArray()
		}
	}

	internal interface DatePatternElement {
		fun format(calendar: Calendar, builder: StringBuilder)
	}

	internal abstract class CalendarFieldDatePatterElement(private val field: Int, private val length: Int) :
		DatePatternElement {
		private fun formatNumber(number: Int, length: Int) = buildString {
			append(number)
			while (this.length < length) {
				this.insert(0, '0')
			}
		}

		final override fun format(calendar: Calendar, builder: StringBuilder) {
			builder.append(formatNumber(calendar.get(field), this.length))
		}

	}

	internal class Year(length: Int) : CalendarFieldDatePatterElement(Calendar.YEAR, length)

	internal class Month(length: Int) : CalendarFieldDatePatterElement(Calendar.MONTH, length)

	internal class Day(length: Int) : CalendarFieldDatePatterElement(Calendar.DAY_OF_MONTH, length)

	internal class Hour(length: Int) : CalendarFieldDatePatterElement(Calendar.HOUR_OF_DAY, length)

	internal class Minute(length: Int) : CalendarFieldDatePatterElement(Calendar.MINUTE, length)

	internal class Second(length: Int) : CalendarFieldDatePatterElement(Calendar.SECOND, length)

	internal class MilliSeconds(length: Int) : CalendarFieldDatePatterElement(Calendar.MILLISECOND, length)

	internal class Text(val text: String) : DatePatternElement {
		override fun format(calendar: Calendar, builder: StringBuilder) {
			builder.append(text)
		}

	}

	internal object TimeZoneOffset : DatePatternElement {
		override fun format(calendar: Calendar, builder: StringBuilder) {
			builder.append(calendar.timeZone.getOffset(calendar.time.time))
		}
	}


	fun format(time: LocalDate): String = TODO()

	fun format(time: Date): String = buildString {
		val calendar = GregorianCalendar().apply {
			this.time = time
			this.timeZone = this@SafeDateFormat.timeZone
		}
		compiledPattern.forEach {
			it.format(calendar, this)
		}
	}

	fun parse(input: String): Date = TODO()

	fun parseLocalDate(input: String): LocalDate = TODO()

}
