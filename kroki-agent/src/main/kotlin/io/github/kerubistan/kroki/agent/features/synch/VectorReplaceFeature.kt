package io.github.kerubistan.kroki.agent.features.synch

import io.github.kerubistan.kroki.agent.features.Feature

object VectorReplaceFeature : Feature {
	override val name: String = "Vector_replace"
	override val helpText: String = "Replaces usage of java.util.Vector where safely possible with a List"
	override val defaultEnabled: Boolean = false
}