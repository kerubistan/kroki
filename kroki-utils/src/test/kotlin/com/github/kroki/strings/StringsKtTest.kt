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

    @Test
    fun substringsBetween() {
        assertEquals("world", "hello world!".substringBetween("hello ", "!"))
        assertEquals("something", "blah blah (something) blah".substringBetween("(", ")"))
        assertEquals("blah", "blah)".substringBetween("(", ")"))
        assertEquals("", "blah".substringBetween("(", ")"))
        assertEquals("blah", "(blah".substringBetween("(", ")"))

    }

    @Test
    fun remove() {
        assertEquals("foo bar baz", "foo, bar, baz".remove(",".toRegex()))
        assertEquals("abcdefgh", "abc12def45gh6".remove("\\d+".toRegex()))
        assertEquals("123456", "12   3  45\t6".remove("\\s+".toRegex()))
    }

}