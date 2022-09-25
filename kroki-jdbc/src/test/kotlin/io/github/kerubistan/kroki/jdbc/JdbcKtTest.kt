package io.github.kerubistan.kroki.jdbc

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

internal class JdbcKtTest {

	@Test
	fun useDataSource() {
		val dataSource = mock<DataSource>()
		val connection = mock<Connection>()
		whenever(dataSource.connection).thenReturn(connection)
		val result = dataSource.use {
			assertEquals(connection, this)
			"TEST"
		}
		assertEquals("TEST", result)
	}

	@Test
	fun queryConnection() {
		val connection = mock<Connection>()
		val statement = mock<Statement>()
		val resultSet = mock<ResultSet>()
		whenever(resultSet.next()).thenReturn(true, false)
		whenever(connection.createStatement()).thenReturn(statement)
		whenever(statement.executeQuery(any())).thenReturn(resultSet)
		val result = connection.query("SELECT 1") { 1 }
		assertEquals(listOf(1), result)
		verify(connection).createStatement()
		verify(statement).executeQuery(eq("SELECT 1"))
		verify(resultSet, times(2)).next()
		verify(resultSet).close()
	}

	@Test
	fun queryPreparedStatement() {
		val connection = mock<Connection>()
		val statement = mock<PreparedStatement>()
		val resultSet = mock<ResultSet>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		whenever(statement.executeQuery()).thenReturn(resultSet)
		whenever(resultSet.next()).thenReturn(true, false)
		whenever(resultSet.getInt(eq(1))).thenReturn(1)
		whenever(resultSet.getString(eq(1))).thenReturn("TEST-EMPLOYEE")

		connection.query(
			"SELECT ID, NAME FROM EMPLOYEE WHERE SALARY < ? AND CONTRACT_TYPE = ?",
			1000, "part-time"
		) {
			getInt(1) to getString(2)
		}

		verify(connection).prepareStatement(eq("SELECT ID, NAME FROM EMPLOYEE WHERE SALARY < ? AND CONTRACT_TYPE = ?"))
		verify(statement).setInt(eq(1), eq(1000))
		verify(statement).setString(eq(2), eq("part-time"))
		verify(statement).executeQuery()
		verify(statement).close()
		verify(resultSet).close()
	}

	@Test
	fun toList() {
		val resultSet = mock<ResultSet>()
		whenever(resultSet.next()).thenReturn(true, true, true, false)
		whenever(resultSet.getString(1)).thenReturn("a", "b", "c")

		val list = resultSet.toList {
			getString(1)
		}

		assertEquals(listOf("a", "b", "c"), list)
	}

	@Test
	fun query() {
		val connection = mock<Connection>()
		val statement = mock<PreparedStatement>()
		val resultSet = mock<ResultSet>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		whenever(statement.executeQuery()).thenReturn(resultSet)
		val count = 1

		val results = connection.query({ "select ${param(count)} from dual" }) { this }

		verify(connection).prepareStatement(eq("select ? from dual"))
		verify(statement).executeQuery()
		verify(statement).close()
		assertEquals(resultSet, results)
	}

	@Test
	fun queryAlternative() {
		val connection = mock<Connection>()
		val statement = mock<PreparedStatement>()
		val resultSet = mock<ResultSet>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		whenever(statement.executeQuery()).thenReturn(resultSet)
		val count = 1

		val results = connection.query({ "select ${count.param} from dual" }) { this }

		verify(connection).prepareStatement(eq("select ? from dual"))
		verify(statement).executeQuery()
		verify(statement).close()
		assertEquals(resultSet, results)
	}

	@Test
	fun insert() {
		val connection = mock<Connection>()
		val statement = mock<PreparedStatement>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		whenever(statement.executeUpdate()).thenReturn(1)
		val message = "TEST - all ok"
		val severity = "INFO"

		val results = connection.insert {
			"""
			insert into events(id, timestamp, message, severity) 
				values
				(events.nextval, ${param(1.toLong())}, ${param(message)}, ${param(severity)})
			""".trimIndent()
		}

		verify(connection).prepareStatement(
			"""
			insert into events(id, timestamp, message, severity) 
				values
				(events.nextval, ?, ?, ?)
		""".trimIndent()
		)
		verify(statement).executeUpdate()
		verify(statement).close()
		assertEquals(1, results)
	}

	@Test
	fun update() {
		val price = 100
		val id = 1234L
		val connection = mock<Connection>()
		val statement = mock<PreparedStatement>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		whenever(statement.executeUpdate()).thenReturn(1)

		val results = connection.insert {
			"""
			update products set price = ${param(price)}
			where id = ${param(id)}
			""".trimIndent()
		}

		assertEquals(1, results)
		verify(connection).prepareStatement(
			eq(
				"""
			update products set price = ?
			where id = ?
		""".trimIndent()
			)
		)
		verify(statement).setInt(eq(1), eq(price))
		verify(statement).setLong(eq(2), eq(id))
		verify(statement).close()
	}

	@Test
	fun queryDataSource() {
		val dataSource: DataSource = mock()
		val id: Long = 1
		val connection = mock<Connection>()
		whenever(dataSource.connection).thenReturn(connection)
		val statement = mock<PreparedStatement>()
		whenever(connection.prepareStatement(any())).thenReturn(statement)
		val resultSet = mock<ResultSet>()
		whenever(statement.executeQuery()).thenReturn(resultSet)
		whenever(resultSet.next()).thenReturn(true, true, true, false)
		whenever(resultSet.getString(eq("test_column"))).thenReturn("A", "B", "C")
		val values = dataSource.query("select test_column from test_table where id = ?", id) {
			getString("test_column")
		}

		kotlin.test.assertEquals(listOf("A", "B", "C"), values)
		verify(connection).close()
		verify(resultSet, times(4)).next()
		verify(resultSet).close()
		verify(statement).setLong(eq(1), eq(id))
		verify(statement).close()
	}

}
