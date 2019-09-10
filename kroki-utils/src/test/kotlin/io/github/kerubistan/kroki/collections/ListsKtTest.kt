package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class ListsKtTest {
    @Test
    fun join() {
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A"), listOf("B", "C", "D")).join())
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A", "B"), listOf("C", "D")).join())
        assertEquals(listOf(), listOf(listOf(), listOf<String>()).join())
    }

    @Test
    fun percentile() {
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(-1.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(0.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(100.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(101.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            listOf<Int>().percentile(90.0) {it}
        }

        assertEquals(99, (0..100).toList().shuffled().percentile(99.0) {it})
    }

}