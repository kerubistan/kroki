package com.github.kroki.exceptions

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException

internal class ExceptionsKtTest {

    @Test
    fun getStackTraceAsString() {
        val stackTrace = IllegalArgumentException("TEST").getStackTraceAsString()
        assertTrue(stackTrace.contains("TEST"))
    }
}