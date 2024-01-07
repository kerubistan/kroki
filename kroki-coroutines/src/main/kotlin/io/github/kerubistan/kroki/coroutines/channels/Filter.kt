package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal fun <T> CoroutineScope.filter(inputChannel: Channel<T>, filterLogic: (T) -> Boolean): Channel<T> {
    val filtered = Channel<T>(64)

    launch {
        for (input in inputChannel) {
            if (filterLogic(input)) {
                filtered.send(input)
            }
        }
        filtered.close()

    }
    return filtered
}