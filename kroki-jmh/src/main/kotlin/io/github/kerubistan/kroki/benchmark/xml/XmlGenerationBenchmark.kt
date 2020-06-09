package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.xml
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class XmlGenerationBenchmark {
    @Benchmark
    fun generate(hole : Blackhole) {
        hole.consume(
            xml("root") {
                ! "comment"
                tag("tag", "attribute" to "value")
            }.reader().readText()
        )
    }
}