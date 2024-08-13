package io.github.kerubistan.kroki.agent.features

import io.github.kerubistan.kroki.agent.features.synch.StringBufferReplaceFeature
import io.github.kerubistan.kroki.agent.features.synch.StringWriterReplaceFeature
import io.github.kerubistan.kroki.agent.features.synch.VectorReplaceFeature
import io.github.kerubistan.kroki.agent.features.threadsafety.SimpleDateFormatReplaceFeature

val features: Set<Feature> = setOf(
	VectorReplaceFeature,
	StringBufferReplaceFeature,
	StringWriterReplaceFeature,
	SimpleDateFormatReplaceFeature
)