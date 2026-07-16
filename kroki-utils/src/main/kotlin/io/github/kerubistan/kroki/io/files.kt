package io.github.kerubistan.kroki.io

import java.io.File

/**
 * Allows navigating a directory by the / operator, which reads like the unix directory separator.
 * For example userHome / ".ssh" is less brain overload than File(userHome, ".ssh").
 * @sample io.github.kerubistan.kroki.io.FilesSamples.easyNavigationWithDivOperator
 */
operator fun File.div(name: String) = File(this, name)

/**
 * The user home directory.
 */
val userHome = File(System.getProperty("user.home"))

/**
 * The working directory of the java process.
 */
val workingDirectory = File(System.getProperty("user.dir"))
