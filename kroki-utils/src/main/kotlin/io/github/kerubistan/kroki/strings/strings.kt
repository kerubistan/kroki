package io.github.kerubistan.kroki.strings

import java.util.*

private val lines = arrayOf(8, 13, 18, 23)
private val digits = arrayOf((0..7), (9..12), (14..17), (19..22), (24..35))

fun String.isUUID() =
    this.length == 36
            && lines.all { idx -> this[idx] == '-' }
            && digits.all { range ->
        range.all { idx ->
            this[idx].let { char -> char in '0'..'9' || char in 'a'..'f' || char in 'A'..'F' }
        }
    }

fun String.toUUID(): UUID =
    UUID.fromString(this)

private const val notFound = -1

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

fun String.remove(regex: Regex) = this.replace(regex, "")
