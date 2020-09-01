package io.github.kerubistan.kroki.bytes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BytesKtTest {

	@Test
	fun toBase64() {
		val array = ByteArray(4)
		array[0] = 0
		array[1] = 1
		array[2] = 2
		array[3] = 3
		assertEquals("AAECAw==", array.toBase64())
	}
}