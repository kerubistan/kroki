package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.mergeInstancesWith
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.UUID.randomUUID

@State(Scope.Benchmark)
open class MergeBenchmark {

    data class Hat(
        val color: String,
        val size: Long,
        val ownerId: UUID
    )

    data class Person(
        val id : UUID = randomUUID(),
        val name : String,
        val hat : Hat? = null
    )

    @Param("0", "1", "2", "4", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096")
    var nrOfPersons = 0
    @Param("0", "1", "2", "4", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096")
    var nrOfHats = 0

    var people = listOf<Person>()
    var hats = listOf<Hat>()

    @Setup
    fun setup() {
        people = (0..nrOfPersons).map {
            Person(
                id = randomUUID(),
                name = "blah"
            )
        }.shuffled()
        hats = (0..nrOfHats).map {
            idx ->
            Hat(
                size = 1,
                color = "blue",
                ownerId = if(idx < nrOfPersons) {people[idx].id} else { randomUUID() }
            )
        }.shuffled()
    }

    @Benchmark
    fun merge(hole : Blackhole) {
        hole.consume(
            people.mergeInstancesWith(
                leftItems = hats,
                leftValue = { it.ownerId },
                rightValue = { it.id },
                merge = { person, hat -> person.copy(hat = hat) }
            )
        )
    }
}