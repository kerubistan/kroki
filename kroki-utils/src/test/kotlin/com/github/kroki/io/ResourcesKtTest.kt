package com.github.kroki.io

import com.github.kroki.time.now
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.StringReader
import kotlin.test.assertEquals

internal class ResourcesKtTest {

    @Test
    fun resourceToString() {
        val data = resourceToString("com/github/kroki/io/urlToStringTest.txt")
        assertEquals("Hello World!", data)
    }

    @Test
    fun resourceToStringNotExisting() {
        assertThrows<IllegalArgumentException> { resourceToString("notexisting-${now()}") }
    }

    @Test
    fun readText() {
        assertEquals("OK", StringReader("OK").readText())
    }
}