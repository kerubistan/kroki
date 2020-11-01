package io.github.kerubistan.kroki.benchmark.flyweight

import io.github.kerubistan.kroki.flyweight.flyWeight
import io.github.kerubistan.kroki.time.now
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import kotlin.random.Random

/**
 * Measures the difference between an algorithm (in this case a sort)
 * accessing flyweight and non-flyweight data.
 * To get an idea what why you see cuch difference, try running this
 * benchmark with linux perf tool like this
 * sudo perf stat -B -e cache-references,cache-misses java -Djmh.ignoreLock=true \
 * -jar target/benchmarks.jar FlyWeightBenefitBenchmark -f 1 -p differentValues=1024 \
 * -p size=4096 -p flyWeight=false
 *
 * and you should see something like
 *  99’456’218      cache-misses              #    6.837 % of all cache refs
 * in one case while
 *  106’758’220      cache-misses              #   15.986 % of all cache refs
 * in the other.
 */
@State(Scope.Benchmark)
open class FlyWeightBenefitBenchmark {

	@Param("16", "64", "256", "1024", "4069", "16384", "65536", "262144")
	var size : Int = 0

	@Param("16", "64", "256", "1024", "4069", "16384", "65536", "262144")
	var differentValues = 0

	@Param("true", "false")
	var flyWeight : Boolean = false

	lateinit var list : List<String>

	@Setup
	fun setup() {
		val random = Random(now())
		list = (0..size).map { "value-${random.nextInt(differentValues)}" }
		if(flyWeight) {
			list = list.flyWeight()
		}
	}

	@Benchmark
	fun sort(hole : Blackhole) {
		hole.consume(list.sorted())
	}

}