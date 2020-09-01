package io.github.kerubistan.kroki.objects

import org.junit.Test
import kotlin.test.assertEquals

internal class ObjectsKtTest {
	data class Folder(
		val name: String,
		val subFolders: List<Folder> = listOf()
	)

	@Test
	fun browse() {
		assertEquals(
			setOf("var", "etc"),
			Folder("/", listOf(Folder("var"), Folder("etc"), Folder("home")))
				.find(selector = Folder::subFolders) { it.name.length == 3 }
				.map { it.name }.toSet()
		)

		assertEquals(
			setOf("var", "etc", "home"),
			Folder("/", listOf(Folder("var"), Folder("etc"), Folder("home")))
				.find(selector = Folder::subFolders)
				.map { it.name }.toSet()
		)
	}

}