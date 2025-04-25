package io.github.kerubistan.kroki.benchmark.collections

import com.google.common.collect.ImmutableMap
import io.github.kerubistan.kroki.collections.buildImmutableMap
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.Collections

@State(Scope.Benchmark)
open class ImmutableMapBenchmark {
	@Param("immutablemap", "unmodifiablemap", "guava", "hashmap", "wrapped-hashmap")
	var type = "immutablemap"

	@Param("0", "1", "2")
	var size = 0

	@Param("0")
	var getKey = "0"

	var map: Map<String, String> = mapOf()

	@Setup
	fun setup() {
		val rawMap = buildMap {
			repeat(size) { idx ->
				put(idx.toString(), idx.toString())
			}
		}
		map = when (type) {
			"immutablemap" -> buildImmutableMap { rawMap.forEach { (key, value) -> put(key, value) } }
			"unmodifiablemap" -> Collections.unmodifiableMap(map)
			"guava" -> ImmutableMap.builder<String, String>().apply {
				rawMap.forEach { (key, value) -> put(key, value) }
			}
				.build()

			"hashmap" -> buildMap { rawMap.forEach { s, s2 -> put(s, s2) } }
			"wrapped-hashmap" -> Collections.unmodifiableMap(rawMap)
			else -> throw IllegalArgumentException("not known: $type")
		}
	}

	@Benchmark
	fun get(blackhole: Blackhole) {
		blackhole.consume(map[getKey])
	}

	@Benchmark
	fun containsKey(blackhole: Blackhole) {
		blackhole.consume(map.containsKey(getKey))
	}

	@Benchmark
	fun iterateOnKeys(blackhole: Blackhole) {
		map.keys.forEach {
			blackhole.consume(it)
		}
	}

	@Benchmark
	fun iterateOnEntries(blackhole: Blackhole) {
		map.entries.forEach {
			blackhole.consume(it)
		}
	}

	@Benchmark
	fun forEach(blackhole: Blackhole) {
		map.forEach {
			blackhole.consume(it.key)
		}
	}

}