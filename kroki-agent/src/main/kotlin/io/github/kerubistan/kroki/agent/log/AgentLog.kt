package io.github.kerubistan.kroki.agent.log

object AgentLog {

	private const val PREFIX = "kroki-agent"

	fun log(category: Category, message: String) {
		println("$PREFIX: [$category] - $message")
	}
}