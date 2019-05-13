package com.github.kroki.strings

import java.util.*

private val uuidPattern = "([0-9]|[a-f]){8}-([0-9]|[a-f]){4}-([0-9]|[a-f]){4}-([0-9]|[a-f]){4}-([0-9]|[a-f]){12}"
    .toRegex()

fun String.isUUID() = this.matches(uuidPattern)

fun String.toUUID(): UUID =
    UUID.fromString(this)
