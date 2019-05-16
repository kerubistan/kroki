package com.github.kroki.strings

import java.util.*

private val uuidPattern = "([0-9]|[a-f]){8}-([0-9]|[a-f]){4}-([0-9]|[a-f]){4}-([0-9]|[a-f]){4}-([0-9]|[a-f]){12}"
    .toRegex()

fun String.isUUID() = this.matches(uuidPattern)

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
