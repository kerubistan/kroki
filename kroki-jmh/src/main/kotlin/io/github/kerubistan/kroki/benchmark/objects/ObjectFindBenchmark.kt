package io.github.kerubistan.kroki.benchmark.objects

import io.github.kerubistan.kroki.objects.find
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ObjectFindBenchmark {

	data class Folder(
		val name: String,
		val subFolders: List<Folder> = listOf()
	)

	val tree = Folder(
		"/",
		listOf(
			Folder("var"),
			Folder("etc"),
			Folder("home")
		)
	)

	@Benchmark
	fun find(hole: Blackhole) {
		tree.find(Folder::subFolders) { it.name == "home" }
	}
}