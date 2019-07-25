package io.github.kerubistan.kroki.numbers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NumbersKtTest {

    @Test
    fun times() {
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2.toBigInteger())
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2)
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2.toLong())
        assertEquals(1.2, ("0.6".toBigDecimal() * 2.toFloat()).toDouble())
        assertEquals(1.2, ("0.6".toBigDecimal() * 2.toDouble()).toDouble())
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2.toShort())
        assertEquals("1.2".toBigDecimal(), "0.6".toBigDecimal() * 2.toByte())
        assertEquals(1024.toBigInteger(), 512.toBigInteger() * 2)
        assertEquals(1024.0.toBigDecimal(), 512.toBigInteger() * 2.0)
    }

    @Test
    fun compareTo() {

        assertTrue("1.0001".toBigDecimal() > 1.toBigInteger())
        assertTrue("0.999999".toBigDecimal() < 1.toBigInteger())
        assertTrue("1.0001".toBigDecimal() > 1)
        assertTrue("1.0001".toBigDecimal() > 1.toLong())
        assertTrue("1.0001".toBigDecimal() > 1.toShort())
        assertTrue("1.0001".toBigDecimal() > 1.toByte())
        assertTrue("1.01".toBigDecimal() > 1.001)
        assertTrue("1.01".toBigDecimal() > 1.001.toFloat())


        assertTrue(1.toBigInteger() < "1.0001".toBigDecimal())
    }

}