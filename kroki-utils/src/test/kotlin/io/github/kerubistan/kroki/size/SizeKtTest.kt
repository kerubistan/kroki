package io.github.kerubistan.kroki.size

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SizeKtTest {
    @Test
    fun checkSizes() {
        assertEquals(1.KB, 1024.toBigInteger())
        assertEquals(1.MB, 1024.KB)
        assertEquals(2.TB, 2048.GB)
        assertEquals(1.PB, 1024.TB)
    }
}