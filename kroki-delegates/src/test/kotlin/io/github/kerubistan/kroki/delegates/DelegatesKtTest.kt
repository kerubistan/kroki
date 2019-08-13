package io.github.kerubistan.kroki.delegates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.lang.Thread.yield
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

internal class DelegatesKtTest {

    @Test
    fun threadLocal() {
        // GIVEN
        val dateStr = "1969-07-16 13:32:00"
        val dateFormat by threadLocal { SimpleDateFormat("yyyy-MM-dd hh:mm:ss") }
        val results = mutableSetOf<Date>()

        // WHEN
        (1..1024).map {
            thread {
                val dates = (1..1024).map {
                    yield()
                    dateFormat.parse(dateStr)
                }.toSet()
                synchronized(results) {
                    results.addAll(dates)
                }
            }
        }.map { it.join() }

        //THEN
        assertEquals(1, results.size)
    }

    @Test
    fun weak() {

        data class Image(val values : Int, val width: Int, val height : Int) {
            val bitmap by weak {
                Array(height) {
                    IntArray(width) {values}
                }
            }
        }

        val images = (1..8192).map { nr ->
            Image(nr, width = 1024, height = 768)
        }
        images.forEach {
            assertNotNull(it.bitmap)
            it.bitmap.forEach { ints ->
                assertNotNull(ints)
            }
        }

        repeat(1024) {
            assertNotNull(images.random().bitmap)
        }

    }
}