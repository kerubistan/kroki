package io.github.kerubistan.kroki.jdbc

import io.github.kerubistan.kroki.objects.use
import java.math.BigDecimal
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

/**
 * Borrow a connection from the datasource, execute operations on it and return the connection to the pool.
 * @param function operations to perform
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.dataSourceUseSample
 */
inline fun <T> DataSource.use(function: Connection.() -> T): T = this.connection.let {
	try {
		it.function()
	} finally {
		it.close()
	}
}

/**
 * Borrow a connection to execute a single query on it, and then return the connection.
 * @param query the SQL query
 * @param mapper the result mapper
 * @return list of mapped results
 */
inline fun <T> DataSource.query(query: String, crossinline mapper: ResultSet.() -> T): List<T> =
	this.use {
		query(query, mapper)
	}

/**
 * Query using the connection with a simple, non-parameterized query and map the results into a list.
 * @param query the SQL query
 * @param mapper the result mapper
 * @return list of mapped results
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.connectionQuerySample
 */
inline fun <R> Connection.query(query: String, crossinline mapper: ResultSet.() -> R) =
	this.createStatement().use { executeQuery(query).use { toList(mapper) } }

/**
 * Query using the connection with a parameterized query, map the results and close the resources used.
 * @param query the parameterized SQL query
 * @param params the parameters to the query
 * @param mapper the result mapper
 * @return the list of mapped results
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.connectionParameterizedQuerySample
 */
inline fun <R> Connection.query(query: String, vararg params: Any, crossinline mapper: ResultSet.() -> R): List<R> =
	this.prepareStatement(query).use {
		params.forEachIndexed { index, param ->
			val jdbcIndex = index + 1
			setParameter(jdbcIndex, param)
		}
		executeQuery().use { toList(mapper) }
	}

fun PreparedStatement.setParameter(jdbcIndex: Int, param: Any) {
	when (param) {
		is String -> setString(jdbcIndex, param)
		is Boolean -> setBoolean(jdbcIndex, param)
		is Int -> setInt(jdbcIndex, param)
		is Long -> setLong(jdbcIndex, param)
		is Float -> setFloat(jdbcIndex, param)
		is Double -> setDouble(jdbcIndex, param)
		is BigDecimal -> setBigDecimal(jdbcIndex, param)
		is Byte -> setByte(jdbcIndex, param)
		is ByteArray -> setBytes(jdbcIndex, param)
		else -> throw IllegalArgumentException("Unknown parameter type ${param.javaClass} $param")
	}
}

/**
 * Map the results into a list.
 * @param mapper the result mapper
 * @return list of mapped results
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.resultSetToList
 */
inline fun <T> ResultSet.toList(crossinline mapper: ResultSet.() -> T): List<T> = buildList {
	while (next()) {
		add(mapper())
	}
}

class QueryBuilder {
	val params = mutableListOf<Any>()
	fun param(value: Any): String {
		params.add(value)
		return "?"
	}
}

/**
 * Create a prepred statement and set parameters.
 * @param builder prepared statement builder
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.connectionParameterizedQuerySample
 */
inline fun Connection.prepareStatement(crossinline builder: QueryBuilder.() -> String): PreparedStatement =
	QueryBuilder().let { queryBuilder ->
		val statement = queryBuilder.builder()
		prepareStatement(statement).apply {
			queryBuilder.params.forEachIndexed { index: Int, param: Any ->
				setParameter(index + 1, param)
			}
		}
	}

/**
 * Run a query using prepared statement.
 * @param builder
 * @return result set
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.queryBuilderSample
 */
inline fun Connection.query(crossinline builder: QueryBuilder.() -> String): ResultSet =
	prepareStatement(builder).use {
		executeQuery()
	}

/**
 * Run an insert operation using prepared statement.
 * @param builder query builder
 * @return number of modified records
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.queryBuilderInsertSample
 */
inline fun Connection.insert(crossinline builder: QueryBuilder.() -> String): Int =
	update(builder)

/**
 * Run an update operation using prepared statement.
 * @param builder query builder
 * @return number of modified records
 * @sample io.github.kerubistan.kroki.jdbc.JdbcSamples.queryBuilderUpdateSample
 */
inline fun Connection.update(crossinline builder: QueryBuilder.() -> String): Int =
	prepareStatement(builder).use {
		executeUpdate()
	}
