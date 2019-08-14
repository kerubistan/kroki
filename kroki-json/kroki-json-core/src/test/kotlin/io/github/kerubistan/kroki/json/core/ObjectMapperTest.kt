package io.github.kerubistan.kroki.json.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ObjectMapperTest {

    @Test
    fun objectToString() {
        assertEquals(""""foo"""", ObjectMapper().objectToString("foo"))
        assertEquals("""{"foo":"bar"}""", ObjectMapper().objectToString(mapOf("foo" to "bar")))
        assertEquals("""{"foo":1}""", ObjectMapper().objectToString(mapOf("foo" to 1)))
        assertEquals("""{"foo":true}""", ObjectMapper().objectToString(mapOf("foo" to true)))
        assertEquals("""{"foo":false}""", ObjectMapper().objectToString(mapOf("foo" to false)))
    }

    @Test
    fun stringToObject() {
        assertEquals(mapOf("foo" to "bar"), ObjectMapper().stringToObject("""{"foo":"bar"}"""))
        assertEquals(mapOf("foo" to 1), ObjectMapper().stringToObject("""{"foo":1}"""))
        assertEquals(mapOf("foo" to true), ObjectMapper().stringToObject("""{"foo":true}"""))
        TODO()
    }
}