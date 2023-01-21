package io.github.kerubistan.kroki.io

class FilesSamples {

	fun easyNavigationWithDivOperator() {
		(userHome / ".ssh" / "known_hosts").readLines().forEach(::println)
	}

}