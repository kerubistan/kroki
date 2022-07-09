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
}