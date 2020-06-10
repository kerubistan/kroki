package io.github.kerubistan.kroki.benchmark.delegates

import io.github.kerubistan.kroki.delegates.ReferenceDelegate
import io.github.kerubistan.kroki.delegates.weak
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class WeakDelegateBenchmark {

	private fun initializer() = 1

	private fun lazyAccess(lazy: Lazy<Int>) =
		(1..1024).map { lazy.value }.max()!!

	private fun weakAccess(weak: ReferenceDelegate<Int>) =
		(1..1024).map { weak.value }.max()!!

	@Benchmark
	fun eager(hole: Blackhole) {
		hole.consume(initializer())
	}

	@Benchmark
	fun lazyNone(hole: Blackhole) {
		hole.consume(lazy(mode = LazyThreadSafetyMode.NONE, initializer = ::initializer).let(::lazyAccess))
	}

	@Benchmark
	fun lazySync(hole: Blackhole) {
		hole.consume(lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED, initializer = ::initializer).let(::lazyAccess))
	}

	@Benchmark
	fun lazyPub(hole: Blackhole) {
		hole.consume(lazy(mode = LazyThreadSafetyMode.PUBLICATION, initializer = ::initializer).let(::lazyAccess))
	}

	@Benchmark
	fun weakNone(hole: Blackhole) {
		hole.consume(weak(mode = LazyThreadSafetyMode.NONE, initializer = ::initializer).let(::weakAccess))
	}

	@Benchmark
	fun weakSync(hole: Blackhole) {
		hole.consume(weak(mode = LazyThreadSafetyMode.SYNCHRONIZED, initializer = ::initializer).let(::weakAccess))
	}

	@Benchmark
	fun weakPub(hole: Blackhole) {
		hole.consume(weak(mode = LazyThreadSafetyMode.PUBLICATION, initializer = ::initializer).let(::weakAccess))
	}

}