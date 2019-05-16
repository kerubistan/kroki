package io.github.kerubistan.kroki.numbers

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NumbersKtTest {

    @Test
    fun times() {
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2.toBigInteger())
    }
}