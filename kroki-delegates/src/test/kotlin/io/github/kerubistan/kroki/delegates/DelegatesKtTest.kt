package io.github.kerubistan.kroki.delegates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Thread.yield
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread
import kotlin.test.assertNull

internal class DelegatesKtTest {

	@Test
	fun threadLocalTest() {
		// GIVEN
		val dateStr = "1969-07-16 13:32:00"
		val dateFormat by threadLocal { SimpleDateFormat("yyyy-MM-dd hh:mm:ss") }
		val results = mutableSetOf<Date>()

		// WHEN
		(1..1024).map {
			thread {
				val dates = (1..1024).map {
					yield()
					dateFormat.parse(dateStr)
				}.toSet()
				synchronized(results) {
					results.addAll(dates)
				}
			}
		}.map { it.join() }

		//THEN
		assertEquals(1, results.size)
	}

	@Test
	fun weak() {

		data class Image(val values: Int, val width: Int, val height: Int) {
			val bitmap by weak {
				Array(height) {
					IntArray(width) { values }
				}
			}
		}

		val images = (1..8192).map { nr ->
			Image(nr, width = 1024, height = 768)
		}
		images.forEach {
			assertNotNull(it.bitmap)
			it.bitmap.forEach { ints ->
				assertNotNull(ints)
			}
		}

		repeat(1024) {
			assertNotNull(images.random().bitmap)
		}

	}

	@Test
	fun soft() {

		data class Image(val values: Int, val width: Int, val height: Int) {
			val bitmap by soft {
				Array(height) {
					IntArray(width) { values }
				}
			}
		}

		val images = (1..8192).map { nr ->
			Image(nr, width = 1024, height = 768)
		}
		images.forEach {
			assertNotNull(it.bitmap)
			it.bitmap.forEach { ints ->
				assertNotNull(ints)
			}
		}

		repeat(1024) {
			assertNotNull(images.random().bitmap)
		}

	}

	private inline fun <reified T : Serializable> serializeDeserialize(obj: T): T {
		val serialized = ByteArrayOutputStream().use { byteArrayOutputStream ->
			ObjectOutputStream(byteArrayOutputStream).use {
				it.writeObject(obj)
			}
			byteArrayOutputStream.toByteArray()
		}

		return ObjectInputStream(ByteArrayInputStream(serialized)).use { it.readObject() as T }
	}

	@Test
	fun weakSerialization() {

		data class BankAccount(
			val accountId: String,
			val balance: Double,
			val currency: String
		) : Serializable

		data class Person(
			val name: String,
			val accounts: List<BankAccount> = listOf()
		) : Serializable {
			val bankAccountsByAccountId by weak { accounts.associateBy { it.accountId } }
		}

		val testPerson = Person(
			name = "Bob",
			accounts = listOf(
				BankAccount(
					accountId = "HUF00001234",
					balance = 12345678.toDouble(),
					currency = "HUF"
				),
				BankAccount(
					accountId = "CHF00003456",
					balance = 20000.toDouble(),
					currency = "CHF"
				)
			)
		)

		val deserialized = serializeDeserialize(testPerson)

		assertEquals(testPerson, deserialized)
		assertEquals(
			mapOf(
				"HUF00001234" to BankAccount(
					accountId = "HUF00001234",
					balance = 12345678.toDouble(),
					currency = "HUF"
				),
				"CHF00003456" to BankAccount(
					accountId = "CHF00003456",
					balance = 20000.toDouble(),
					currency = "CHF"
				)
			),
			deserialized.bankAccountsByAccountId
		)
	}

	@Test
	fun softSerialization() {

		data class BankAccount(
			val accountId: String,
			val balance: Double,
			val currency: String
		) : Serializable

		data class Person(
			val name: String,
			val accounts: List<BankAccount> = listOf()
		) : Serializable {
			val bankAccountsByAccountId by soft { accounts.associateBy { it.accountId } }
		}

		val testPerson = Person(
			name = "Bob",
			accounts = listOf(
				BankAccount(
					accountId = "HUF00001234",
					balance = 12345678.toDouble(),
					currency = "HUF"
				),
				BankAccount(
					accountId = "CHF00003456",
					balance = 20000.toDouble(),
					currency = "CHF"
				)
			)
		)

		val deserialized = serializeDeserialize(testPerson)

		assertEquals(testPerson, deserialized)
		assertEquals(
			mapOf(
				"HUF00001234" to BankAccount(
					accountId = "HUF00001234",
					balance = 12345678.toDouble(),
					currency = "HUF"
				),
				"CHF00003456" to BankAccount(
					accountId = "CHF00003456",
					balance = 20000.toDouble(),
					currency = "CHF"
				)
			),
			deserialized.bankAccountsByAccountId
		)
	}

	@Test
	fun assignableThreadLocal() {
		var x by threadLocal<String>()
		assertNull(x)
		x = "AAAA"
		assertEquals("AAAA", x)
		thread {
			assertNull(x)
			x = "BBBB"
			assertEquals("BBBB", x)
		}.join()
		assertEquals("AAAA", x)
	}

	@Test
	fun assignableThreadLocalWithInitialValue() {
		var x by threadLocal("initial value")
		assertEquals("initial value", x)
		thread {
			assertEquals("initial value", x)
		}.join()
		x = "updated value"
		assertEquals("updated value", x)
		thread {
			assertEquals("initial value", x)
		}.join()
	}

	@Test
	fun atomicReference() {
		var x by atomic<String>(AtomicReference())
		assertNull(x)
		thread {
			assertNull(x)
		}.apply {
			join()
		}
		x = "A"
		assertEquals("A", x)
		thread {
			assertEquals("A", x)
			x = "B"
		}.apply {
			join()
		}
		assertEquals("B", x)
	}
}