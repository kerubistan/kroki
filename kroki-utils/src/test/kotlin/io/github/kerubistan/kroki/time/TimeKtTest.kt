package io.github.kerubistan.kroki.time

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TimeKtTest {

    @Test
    fun nowTest() {
        assertTrue(now() > 0)
    }
}