package io.github.kerubistan.kroki.agent.features.synch

import io.github.kerubistan.kroki.agent.features.Feature

object StringWriterReplaceFeature : Feature {
	override val name: String = "StringWriter_replace"
	override val helpText: String = "Replace StringWriter instances with EasyStringWriter when possible"
	override val defaultEnabled: Boolean = false
}