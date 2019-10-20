package io.github.kerubistan.kroki.strings

import java.util.*

fun String.isUUID() =
    this.length == 36
            && (0..7).all(this::isHexadecimalDigit)
            && this[8] == '-'
            && (9..12).all(this::isHexadecimalDigit)
            && this[13] == '-'
            && (14..17).all(this::isHexadecimalDigit)
            && this[18] == '-'
            && (19..22).all(this::isHexadecimalDigit)
            && this[23] == '-'
            && (24..35).all(this::isHexadecimalDigit)

private fun String.isHexadecimalDigit(idx: Int) =
        this[idx].let { char -> char in '0'..'9' || char in 'a'..'f' || char in 'A'..'F' }

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
