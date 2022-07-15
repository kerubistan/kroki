package io.github.kerubistan.kroki.jdbc

import io.github.kerubistan.kroki.objects.use
import org.apache.derby.jdbc.EmbeddedDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class DerbyJdbcTest {

	lateinit var connection: Connection

	@BeforeEach
	fun setup() {
		Class.forName(EmbeddedDriver::class.java.name)
		connection = DriverManager.getConnection("jdbc:derby:directory:target/testdb;create=true");
	}

	@AfterEach
	fun cleanup() {
		File("target/testdb").apply {
			if (exists()) {
				deleteRecursively()
			}
		}
	}

	@Test
	fun queries() {
		val employeeNameInput = "Bob '; DROP TABLE EMPLOYEE "
		connection.use {
			update { "CREATE TABLE EMPLOYEE(ID INT PRIMARY KEY, NAME VARCHAR(128))" }
			insert { "INSERT INTO EMPLOYEE(ID, NAME) VALUES (1, ${employeeNameInput.param})" }
			query( "SELECT ID, NAME FROM EMPLOYEE" ) {
				forEach { println(" ${getInt("ID")} -> ${getString("NAME")} ") }
			}
		}
	}
}