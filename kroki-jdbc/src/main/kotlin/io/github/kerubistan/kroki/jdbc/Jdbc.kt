package io.github.kerubistan.kroki.jdbc

import io.github.kerubistan.kroki.objects.use
import java.math.BigDecimal
import java.sql.Connection
import java.sql.ResultSet
import javax.sql.DataSource

inline fun <T> DataSource.use(function: Connection.() -> T): T = this.connection.let {
	try {
		it.function()
	} finally {
		it.close()
	}
}

inline fun <T> DataSource.query(query: String, crossinline mapper: ResultSet.() -> T) : List<T> =
	this.use {
		query(query, mapper)
	}

inline fun <R> Connection.query(query: String, crossinline mapper: ResultSet.() -> R) =
	this.createStatement().use { executeQuery(query).use { toList(mapper) } }

inline fun <R> Connection.query(query: String, vararg params: Any, crossinline mapper: ResultSet.() -> R): List<R> =
	this.prepareStatement(query).use {
		params.forEachIndexed { index, param ->
			val jdbcIndex = index + 1
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
		executeQuery().use { toList(mapper) }
	}

inline fun <T> ResultSet.toList(mapper: ResultSet.() -> T): List<T> = buildList {
	while (next()) {
		add(mapper())
	}
}
