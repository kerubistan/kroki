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
		)

		fun compilePattern(pattern: String): Array<DatePatternElement> {
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

	interface DatePatternElement {
		fun format(calendar: Calendar, builder: StringBuilder)
	}

	abstract class CalendarFieldDatePatterElement(private val field: Int, private val length: Int) :
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

	class Year(length: Int) : CalendarFieldDatePatterElement(Calendar.YEAR, length)

	class Month(length: Int) : CalendarFieldDatePatterElement(Calendar.MONTH, length)

	class Day(length: Int) : CalendarFieldDatePatterElement(Calendar.DAY_OF_MONTH, length)

	class Text(val text: String) : DatePatternElement {
		override fun format(calendar: Calendar, builder: StringBuilder) {
			builder.append(text)
		}

	}


	fun format(time: LocalDate): String = TODO()

	fun format(time: Date): String = buildString {
		val calendar = GregorianCalendar().apply { this.time = time }
		compiledPattern.forEach {
			it.format(calendar, this)
		}
	}

	fun parse(input: String): Date = TODO()

	fun parseLocalDate(input: String): LocalDate = TODO()

}
