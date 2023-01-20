package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FilesKtTest {

	@Test
	fun divOperator() {
		assertTrue(workingDirectory.exists())
		assertTrue(userHome.exists())
		assertFalse((userHome / "testdir-${randomUUID()}").exists())
		assertFalse((userHome / "testdir-${randomUUID()}" / "somefile").exists())
	}

}