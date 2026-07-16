package io.github.kerubistan.kroki.io

import java.io.InputStream
import java.util.Random

class RandomInputStream(
	private val random: Random = Random(),
	private val length: Int = random.nextInt()
) : InputStream() {
	private var counter = 0
	override fun read(): Int {
		return if (counter >= length) {
			-1
		} else {
			counter++
			random.nextInt()
		}
	}

	override fun read(buffer: ByteArray): Int {
		val available = length - counter
		return when {
			available <= 0 -> {
				-1
			}

			buffer.size < available -> {
				random.nextBytes(buffer)
				counter += buffer.size
				buffer.size
			}

			else -> {
				val temp = ByteArray(available)
				random.nextBytes(temp)
				temp.copyInto(buffer)
				counter += available
				available
			}
		}
	}
}