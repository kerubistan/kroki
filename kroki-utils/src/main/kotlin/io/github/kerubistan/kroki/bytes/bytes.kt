package io.github.kerubistan.kroki.bytes

fun ByteArray.toBase64(): String = java.util.Base64.getEncoder().encodeToString(this)
