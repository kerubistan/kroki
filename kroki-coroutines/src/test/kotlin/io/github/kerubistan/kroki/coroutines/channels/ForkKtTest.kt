package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ForkKtTest {

	@Test
	fun fork() {
		runBlocking {
			val (left, right) = fork(produce {
				for (letter in 'A' until 'Z') {
					send(letter.toString())
				}
			})
			for (leftMessage in left) {
				val rightMessage = right.receive()
				assertEquals(leftMessage, rightMessage)
			}
		}
	}
}