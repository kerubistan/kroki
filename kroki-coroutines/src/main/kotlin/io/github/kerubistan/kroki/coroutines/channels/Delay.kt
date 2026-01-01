package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> CoroutineScope.delay(channel: Channel<T>, capacity: Int = 1024, delayMillis: (T) -> Long): Channel<T> {

	val jobs = Channel<Job>(capacity)
	val outChannel = Channel<T>(capacity)

	launch {
		for (message in channel) {
			val calculatedDelay = delayMillis(message)
			if (calculatedDelay > 0) {
				jobs.send(
					launch {
						delay(calculatedDelay)
						outChannel.send(message)
					}
				)
			} else {
				outChannel.send(message)
			}
		}
		jobs.close()
	}

	launch {
		for (job in jobs) {
			job.join()
		}
		outChannel.close()
	}

	return outChannel
}