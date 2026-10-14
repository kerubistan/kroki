package io.github.kerubistan.kroki.benchmark.reflections

import io.github.kerubistan.kroki.reflection.isEnum
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class IsEnumBenchmark {

	enum class MyEnum {
		FOO
	}

	data class MyData(val id : String)

	@Benchmark
	fun isEnumEnum(hole : Blackhole) {
		hole.consume(MyEnum::class.isEnum)
	}

	@Benchmark
	fun isDataEnum(hole : Blackhole) {
		hole.consume(MyData::class.isEnum)
	}

}