package com.github.kroki.strings

import org.junit.Test
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StringsKtTest {
    @Test
    fun isUUID() {
        assertTrue(randomUUID().toString().isUUID())
        assertFalse(("something else").isUUID())
    }

    @Test
    fun toUUID() {
        val uuid = randomUUID()
        assertEquals(uuid, uuid.toString().toUUID())
    }

}