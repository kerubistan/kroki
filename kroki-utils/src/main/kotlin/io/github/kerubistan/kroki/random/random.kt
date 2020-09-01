package io.github.kerubistan.kroki.random

import java.util.*

private val pwdCharacters = (
		('a'..'z').toList() +
				('A'..'Z').toList() +
				('0'..'9').toList() +
				listOf('_', '-', '.')).toCharArray()

private val globalRandom = Random()

fun genPassword(length: Int = 16, rnd: Random = globalRandom, characters: CharArray = pwdCharacters) =
	buildString(length) {
		repeat(length) {
			append(pwdCharacters[rnd.nextInt(characters.size - 1)])
		}
	}
