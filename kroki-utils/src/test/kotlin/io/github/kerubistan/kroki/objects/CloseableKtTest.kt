package io.github.kerubistan.kroki.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class CloseableKtTest {

	@Test
	fun useIt() {

		assertEquals("blah",
			StringWriter().useIt {
				write("blah")
				toString()
			}
		)

	}
}