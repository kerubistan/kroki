package io.github.kerubistan.kroki.jdbc

import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet
import javax.sql.DataSource

class JdbcSamples {
	fun dataSourceUseSample(dataSource: DataSource) {
		dataSource.use {
			query("select sysdate from dual") {
				getDate(1)
			}
		}
	}

	fun connectionQuerySample(connection: Connection) {
		data class Employee(val id: Long, val name: String, val dateOfBirth: Date)
		connection.query("select id, name, date_of_birth from employees") {
			Employee(
				id = getLong("id"),
				name = getString("name"),
				dateOfBirth = getDate("date_of_birth")
			)
		}
	}

	fun connectionParameterizedQuerySample(connection: Connection) {
		data class Employee(val id: Long, val name: String, val dateOfBirth: Date)
		connection.query(
			"select id, name, date_of_birth from employees where name like ?",
			"Bob%"
		) {
			Employee(
				id = getLong("id"),
				name = getString("name"),
				dateOfBirth = getDate("date_of_birth")
			)
		}
	}

	fun resultSetToList(resultSet: ResultSet) {
		resultSet.toList {
			getInt("id") to getString("name")
		}
	}
}