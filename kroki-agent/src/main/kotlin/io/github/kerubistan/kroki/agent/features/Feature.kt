package io.github.kerubistan.kroki.agent.features

import io.github.kerubistan.kroki.agent.log.AgentLog
import io.github.kerubistan.kroki.agent.log.Category
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.utility.JavaModule
import java.security.ProtectionDomain

interface Feature {

	fun transform(
		builder: DynamicType.Builder<*>,
		typeDescription: TypeDescription,
		classLoader: ClassLoader?,
		javaModule: JavaModule?,
		protectionDomain: ProtectionDomain?
	) {
		AgentLog.log(Category.INFRA, "${this.name} not implemented")
	}

	val name : String
	val helpText : String
	val defaultEnabled : Boolean
}