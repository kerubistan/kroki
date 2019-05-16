package io.github.kerubistan.kroki.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapsKtTest {

    @Test
    fun upsert() {
        assertEquals(
            mapOf("A" to 1, "B" to 2),
            mapOf("A" to 1).upsert("B", init = { 2 }, mapper = { 3 /*not called in this case*/ })
        )
        assertEquals(
            mapOf("A" to 1, "B" to 2),
            mapOf("A" to 1, "B" to 1).upsert("B", init = { 3 /*not called in this case*/ }, mapper = { it + 1 })
        )
    }
}