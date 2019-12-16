package io.github.kerubistan.kroki.parallel

import io.github.kerubistan.kroki.numbers.sumBy
import kotlinx.coroutines.asCoroutineDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CollectionsKtTest {

    lateinit var context: CoroutineContext
    lateinit var exec: ExecutorService

    @BeforeEach
    fun setup() {
        exec = Executors.newSingleThreadExecutor()
        context = exec.asCoroutineDispatcher()
    }

    @AfterEach
    fun cleanup() {
        exec.shutdown()
    }

    @Test
    fun pSum() {
        (0 until 5000000).map { BigInteger.ONE }.toList().let { list ->
            assertEquals(list.sumBy { it }, list.pSum(context))
        }
        val random = java.util.Random()
        (0 until 50000).map { random.nextInt().toBigInteger() }.toList().let { list ->
            assertEquals(list.sumBy { it }, list.pSum(context))
        }
    }

}