package io.github.kerubistan.kroki.random

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class RandomKtTest {

    @Test
    fun genPassword() {
        assertEquals(4, genPassword(4).length)
        assertEquals(8, genPassword(8).length)

        assertEquals(0, genPassword(0).length)
        assertEquals(1, genPassword(1).length)

        assertTrue(
            genPassword(length = 10, characters = charArrayOf('a', 'b')).matches("([ab]){10}".toRegex())
        )
    }
}