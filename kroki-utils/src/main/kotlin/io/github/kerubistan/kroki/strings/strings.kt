package io.github.kerubistan.kroki.strings

import java.util.*

private val uuidFormatLines = arrayOf(8, 13, 18, 23)
private val uuidFormatDigits = arrayOf((0..7), (9..12), (14..17), (19..22), (24..35))

/**
 * Check if a string is in UUID format.
 */
fun String.isUUID() =
	this.length == 36
			&& uuidFormatLines.all { idx -> this[idx] == '-' }
			&& uuidFormatDigits.all { range ->
		range.all { idx ->
			this[idx].let { char -> char in '0'..'9' || char in 'a'..'f' || char in 'A'..'F' }
		}
	}

/**
 * Converts a String to UUID - convenience function for better readability.
 */
fun String.toUUID(): UUID =
	UUID.fromString(this)

private const val notFound = -1

/**
 * Extract a substring from between a prefix and a postfix.
 * @param prefix the prefix
 * @param postfix the postfix
 * @return the substring or an empty string if not found
 */
fun String.substringBetween(prefix: String, postfix: String): String {
	val start = this.indexOf(prefix)
	return if (start == notFound) {
		val end = this.indexOf(postfix)
		if (end == notFound) {
			""
		} else {
			this.substring(0, end)
		}
	} else {
		val end = this.indexOf(postfix, start + prefix.length)
		if (end == notFound) {
			this.substring(start + prefix.length)
		} else {
			this.substring(start + prefix.length, end)
		}
	}
}

/**
 * Remove a part of a string matching a certain regular expression.
 * This is for readability as it better expresses intent than replace with empty string.
 */
fun String.remove(regex: Regex) = this.replace(regex, "")
