package com.github.kroki.benchmark.io

import com.github.kroki.io.EasyStringWriter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EasyStringWriterTest {

    @Test
    fun write() {
            EasyStringWriter().use {
                it.write("foo")
                assertEquals(
                    "foo",
                    it.toString()
                )
            }
    }
}