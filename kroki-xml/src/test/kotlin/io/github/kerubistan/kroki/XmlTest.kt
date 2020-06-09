package io.github.kerubistan.kroki

import org.junit.Test

class XmlTest {
    @Test
    fun generation() {
        val generated = xml("test") {
            ! "comment"
            tag("hello", "a" to "1", "b" to "2") {
                cdata("world")
            }
            comment("there is a horse in my garden")
            tag("hello") { - "world" }
            tag("hello") {
                ! "lazy"
                + "world"
            }
            text("")
            text { ('a'..'z').forEach { append(it) } }
        }.reader().readText()
        println(generated)
    }

}