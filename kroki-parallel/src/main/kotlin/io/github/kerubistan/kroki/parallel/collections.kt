package io.github.kerubistan.kroki.parallel

import io.github.kerubistan.kroki.numbers.sumBy
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import kotlin.coroutines.CoroutineContext

private const val defaultBlockSize = 128 * 1024

fun List<BigInteger>.pSum(context: CoroutineContext, blockSize: Int = defaultBlockSize) =
    if (this.size < blockSize) this.sumBy { it } else runBlocking(context) {
        (this@pSum.indices.first until this@pSum.indices.last step blockSize).map { start ->
            val end = (start + blockSize).coerceAtMost(this@pSum.indices.last + 1)
            async(context) {
                var sum = ZERO
                for (index in start until end) {
                    sum += this@pSum[index]
                }
                sum
            }
        }.map { it.await() }.sumBy { it }
    }

fun List<BigInteger>.tSum(executor : ExecutorService, blockSize: Int = defaultBlockSize) =
    if (this.size < blockSize) this.sumBy { it } else
        (this.indices.first until this.indices.last step blockSize).map { start ->
            val end = (start + blockSize).coerceAtMost(this.indices.last + 1)
            executor.submit(Callable {
                var sum = ZERO
                for (index in start until end) {
                    sum += this[index]
                }
                sum
            })
        }.map { it.get() }.sumBy { it }
