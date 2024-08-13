package io.github.kerubistan.kroki.agent.features.synch

import io.github.kerubistan.kroki.agent.features.Feature

object StringBufferReplaceFeature : Feature {
	override val name: String = "StringBuffer_replace"
	override val helpText: String = "Replace StringBuffer use with StringBuilder where safe"
	override val defaultEnabled: Boolean = false
}