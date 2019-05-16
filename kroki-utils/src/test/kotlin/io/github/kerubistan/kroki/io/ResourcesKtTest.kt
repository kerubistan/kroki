package io.github.kerubistan.kroki.io

import io.github.kerubistan.kroki.time.now
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.StringReader
import kotlin.test.assertEquals

internal class ResourcesKtTest {

    @Test
    fun resourceToString() {
        val data = resourceToString("io/github/kerubistan/kroki/io/urlToStringTest.txt")
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