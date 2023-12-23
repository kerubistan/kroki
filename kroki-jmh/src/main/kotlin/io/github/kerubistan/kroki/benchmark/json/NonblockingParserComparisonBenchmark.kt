package io.github.kerubistan.kroki.benchmark.json

import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.util.*

@State(Scope.Benchmark)
open class NonblockingParserComparisonBenchmark {

	@Param("1", "4", "16", "64", "256", "1024", "4096")
	var size : Int = 1
	private val objectMapper = ObjectMapper()

	private lateinit var testFile : File

	@Setup
	fun setup() {
		testFile = File.createTempFile(UUID.randomUUID().toString(), ".json")
		testFile.writer().use { writer ->
			objectMapper.writeValue(writer,
				(0 until size).associate { nr -> "item-$nr" to "value-$nr" }
			)
		}
	}

	@TearDown
	fun cleanup() {
		testFile.delete()
	}

	@Benchmark
	fun blockingParse(hole : Blackhole) {
		objectMapper.createParser(testFile).let {
			var lastToken = it.nextToken()
			while (lastToken != JsonToken.END_OBJECT) {
				lastToken = it.nextToken()
			}
		}
	}

	@Benchmark
	fun nonBlockingParse(hole : Blackhole) {
		val byteBuffer = ByteBuffer.allocate(4096)
		ObjectMapper().createNonBlockingByteArrayParser().use {
			parser ->
			parser as NonBlockingJsonParser
			RandomAccessFile(testFile.absoluteFile, "r").channel.use {
				channel ->
				while (channel.position() < channel.size()) {
					val bufferSize = channel.read(byteBuffer)
					val array = byteBuffer.array()
					parser.feedInput(array, 0, bufferSize)
					while (!parser.nonBlockingInputFeeder.needMoreInput()) {
						parser.nextToken()
					}
					byteBuffer.flip()
				}
			}
		}
	}

}