package io.github.kerubistan.kroki.jdbc

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource
import kotlin.test.assertEquals

class JdbcSamples {
	fun dataSourceUseSample(dataSource: DataSource) {
		dataSource.use {
			query("select sysdate from dual") {
				getDate(1)
			}
		}
	}

	fun dataSourceQuerySample(dataSource: DataSource) {
		data class Employee(val id: Long, val name: String, val dateOfBirth: Date)
		dataSource.query("select id, name, date_of_birth from employees") {
			Employee(
				id = getLong("id"),
				name = getString("name"),
				dateOfBirth = getDate("date_of_birth")
			)
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

	fun queryBuilderSample(connection: Connection) {
		val minSalary = 5000
		val minHeight = 150
		connection.query({
			"""
				SELECT
					ID,
					NAME
				FROM
					EMPLOYEES
				WHERE
					SALARY < ${param(minSalary)}
					OR HEIGHT < ${param(minHeight)} 
			""".trimIndent()
		}) {
			forEach {
				// process
			}
		}
	}

	fun queryBuilderOperatorSample(connection: Connection) {
		val minSalary = 5000
		val minHeight = 150
		val name = "bob"
		connection.query({
			"""
				SELECT
					ID,
					NAME,
					EMAIL
				FROM
					EMPLOYEES
				WHERE
					SALARY < ${minSalary.param}
					OR HEIGHT < ${minHeight.param} 
					OR NAME LIKE ${"%$name%".param}
			""".trimIndent()
		}) {
			forEach {
				// process
			}
		}
	}

	fun queryBuilderInsertSample(connection: Connection) {
		val name = "snakeoil"
		val price = 100
		val categoryId = "foo"
		connection.insert {
			"""
				INSERT INTO PRODUCT(ID, NAME, PRICE, CATEGORY_ID)
				VALUES (PRODUCT_SEQ.nextval(), ${param(name)}, ${param(price)}, ${param(categoryId)} )
			""".trimIndent()
		}
	}

	fun queryBuilderUpdateSample(connection: Connection) {
		val id = 1000
		val name = "super-snakeoil"
		connection.update {
			"""
				UPDATE PRODUCT
				SET
					NAME = ${param(name)},
					PRICE = PRICE * 0.99,
				WHERE
					ID = ${param(id)}
			""".trimIndent()
		}
	}

	fun queryDataSource(dataSource: DataSource, id: Long) {
		dataSource.query("select firstname, lastname from test where id = ?", id) {
		}
	}


}