package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ListsKtTest {
    @Test
    fun join() {
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A"), listOf("B", "C", "D")).join())
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A", "B"), listOf("C", "D")).join())
        assertEquals(listOf(), listOf(listOf(), listOf<String>()).join())
    }

}