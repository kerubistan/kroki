package com.github.kroki.time

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TimeKtTest {

    @Test
    fun nowTest() {
        assertTrue(now() > 0)
    }
}