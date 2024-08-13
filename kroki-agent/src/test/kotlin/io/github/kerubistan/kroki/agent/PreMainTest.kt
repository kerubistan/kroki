package io.github.kerubistan.kroki.agent

import io.github.kerubistan.kroki.agent.features.synch.StringBufferReplaceFeature
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PreMainTest {
	@Test
	fun getEnabledFeatures() {
		assertNotNull(PreMain.getEnabledFeatures(null))
		assertTrue {
			PreMain.getEnabledFeatures("${StringBufferReplaceFeature.name}=true").contains(StringBufferReplaceFeature)
		}
	}
}