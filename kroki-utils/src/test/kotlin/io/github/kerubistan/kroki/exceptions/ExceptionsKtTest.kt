package io.github.kerubistan.kroki.exceptions

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ExceptionsKtTest {

    @Test
    fun getStackTraceAsString() {
        val stackTrace = IllegalArgumentException("TEST").getStackTraceAsString()
        assertTrue(stackTrace.contains("TEST"))
    }
}