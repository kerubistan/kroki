package io.github.kerubistan.kroki

import org.junit.Test

class XmlTest {
    @Test
    fun generation() {
        println(xml("test") {
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
        }.reader().readText())

        println(xml("test").reader().readText())

        println(xml("test") { tag("pass") } .reader().readText())

        println(xml("test") { tag("pass", "really" to true) } .reader().readText())

    }

}