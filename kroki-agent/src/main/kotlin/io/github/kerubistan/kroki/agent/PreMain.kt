package io.github.kerubistan.kroki.agent

import net.bytebuddy.agent.builder.AgentBuilder
import java.lang.instrument.Instrumentation

class PreMain {
	companion object {
		@JvmStatic
		fun premain(argument: String?, instrumentation: Instrumentation) {
			AgentBuilder.Default()
				.installOn(instrumentation)
		}
	}
}
