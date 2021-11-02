package io.github.kerubistan.kroki.benchmark.numbers

import io.github.kerubistan.kroki.numbers.plus
import io.github.kerubistan.kroki.numbers.times
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@State(Scope.Benchmark)
open class BigIntegerOperatorsBenchmark {

	@Param("0", "1", "10", "100")
	var first = "1"

	@Param("0", "1", "10", "100")
	var second = "1"

	lateinit var firstBigInteger: BigInteger
	lateinit var secondBigInteger: BigInteger
	var secondInt: Int = 1
	var secondLong: Long = 1
	var secondShort: Short = 1
	var secondByte: Byte = 1

	@Setup
	fun setup() {
		firstBigInteger = first.toBigInteger()
		secondBigInteger = second.toBigInteger()
		secondInt = second.toInt()
		secondLong = second.toLong()
		secondShort = second.toShort()
		secondByte = second.toByte()
	}

	@Benchmark
	fun bigIntegerTimesLong(hole: Blackhole) {
		hole.consume(firstBigInteger * secondLong)
	}

	@Benchmark
	fun bigIntegerTimesInt(hole: Blackhole) {
		hole.consume(firstBigInteger * secondInt)
	}

	@Benchmark
	fun bigIntegerTimesShort(hole: Blackhole) {
		hole.consume(firstBigInteger * secondShort)
	}

	@Benchmark
	fun bigIntegerTimesByte(hole: Blackhole) {
		hole.consume(firstBigInteger * secondByte)
	}

	@Benchmark
	fun bigIntegerTimesBigInteger(hole: Blackhole) {
		hole.consume(firstBigInteger * secondBigInteger)
	}

	// plus

	@Benchmark
	fun bigIntegerPlusLong(hole: Blackhole) {
		hole.consume(firstBigInteger + secondLong)
	}

	@Benchmark
	fun bigIntegerPlusInt(hole: Blackhole) {
		hole.consume(firstBigInteger + secondInt)
	}

	@Benchmark
	fun bigIntegerPlusShort(hole: Blackhole) {
		hole.consume(firstBigInteger + secondShort)
	}

	@Benchmark
	fun bigIntegerPlusByte(hole: Blackhole) {
		hole.consume(firstBigInteger + secondByte)
	}

	@Benchmark
	fun bigIntegerPlusBigInteger(hole: Blackhole) {
		hole.consume(firstBigInteger + secondBigInteger)
	}

}