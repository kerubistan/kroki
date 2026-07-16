package io.github.kerubistan.kroki.delegates

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread
import kotlin.test.assertNull

class ReferencesKtTest {
	@Test
	fun atomicReference() {
		var x by atomic<String>(AtomicReference())
		assertNull(x)
		thread {
			assertNull(x)
		}.apply {
			join()
		}
		x = "A"
		Assertions.assertEquals("A", x)
		thread {
			Assertions.assertEquals("A", x)
			x = "B"
		}.apply {
			join()
		}
		Assertions.assertEquals("B", x)
	}

	@Test
	fun weak() {

		data class Image(val values: Int, val width: Int, val height: Int) {
			val bitmap by weak {
				Array(height) {
					IntArray(width) { values }
				}
			}
		}

		val images = (1..8192).map { nr ->
			Image(nr, width = 1024, height = 768)
		}
		images.forEach {
			Assertions.assertNotNull(it.bitmap)
			it.bitmap.forEach { ints ->
				Assertions.assertNotNull(ints)
			}
		}

		repeat(1024) {
			Assertions.assertNotNull(images.random().bitmap)
		}

	}

	fun soft() {

		data class Image(val values: Int, val width: Int, val height: Int) {
			val bitmap by soft {
				Array(height) {
					IntArray(width) { values }
				}
			}
		}

		val images = (1..8192).map { nr ->
			Image(nr, width = 1024, height = 768)
		}
		images.forEach {
			Assertions.assertNotNull(it.bitmap)
			it.bitmap.forEach { ints ->
				Assertions.assertNotNull(ints)
			}
		}

		repeat(1024) {
			Assertions.assertNotNull(images.random().bitmap)
		}

	}
}
