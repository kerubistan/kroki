package io.github.kerubistan.kroki.io

import java.io.File

operator fun File.div(name : String) = File(this, name)

val userHome = File(System.getProperty("user.home"))

val workingDirectory = File(System.getProperty("user.dir"))
