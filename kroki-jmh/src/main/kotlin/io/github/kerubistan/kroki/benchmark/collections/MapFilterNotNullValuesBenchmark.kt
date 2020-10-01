package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.filterNotNullValues
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class MapFilterNotNullValuesBenchmark {

	@Param("0", "1", "8",  "64", "1024", "8192")
	var size : Int = 0

	@Param("0", "1", "5", "100")
	var nullOneOutOf : Int = 0

	lateinit var map : Map<String, String?>

	@ExperimentalStdlibApi
	@Setup
	fun setup() {
		var cntr = 0
		map = buildMap {
			(0..size).forEach {
				cntr ++
				if(cntr < nullOneOutOf) {
					put(it.toString(), it.toString())
				} else {
					cntr = 0
					put(it.toString(), null)
				}
			}
		}
	}

	@Benchmark
	fun benchmark(hole : Blackhole) {
		hole.consume(map.filterNotNullValues())
	}
}