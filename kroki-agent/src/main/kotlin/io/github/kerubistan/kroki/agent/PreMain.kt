package io.github.kerubistan.kroki.agent

import java.lang.instrument.Instrumentation

class PreMain {
	companion object {
		@JvmStatic
		fun premain(argument : String?, instrumentation: Instrumentation?) {
			println("fortune, fame, mirror vain, gone insane but the memory premains")
		}
	}
}
