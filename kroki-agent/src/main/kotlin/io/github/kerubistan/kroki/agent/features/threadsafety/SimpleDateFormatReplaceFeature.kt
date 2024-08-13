package io.github.kerubistan.kroki.agent.features.threadsafety

import io.github.kerubistan.kroki.agent.features.Feature

object SimpleDateFormatReplaceFeature : Feature {
	override val name: String = "SimpleDateFormat_replace"
	override val helpText: String = "Replace SimpleDateFormat instances with SafeDateFormat where needed and possible"
	override val defaultEnabled = false
}