package io.github.kerubistan.kroki.collections.jackson

import com.fasterxml.jackson.databind.module.SimpleModule

class KrokiCollectionsModule : SimpleModule() {

	override fun setupModule(context: SetupContext) {
		super.setupModule(context)
		context.addDeserializers(KrokiCollectionsDeserializers())
	}

}