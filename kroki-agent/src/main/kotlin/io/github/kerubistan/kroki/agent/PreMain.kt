package io.github.kerubistan.kroki.agent

import io.github.kerubistan.kroki.agent.features.Feature
import io.github.kerubistan.kroki.agent.features.features
import io.github.kerubistan.kroki.agent.log.AgentLog
import io.github.kerubistan.kroki.agent.log.Category
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.utility.JavaModule
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain

class PreMain {
	companion object {
		@JvmStatic
		fun premain(argument: String?, instrumentation: Instrumentation) {
			val enabledFeatures: Set<Feature> = getEnabledFeatures(argument)

			AgentBuilder.Default()
				.type(ElementMatchers.any())
				.transform { builder: DynamicType.Builder<*>, typeDescription: TypeDescription, classLoader: ClassLoader?, javaModule: JavaModule?, protectionDomain: ProtectionDomain? ->
					AgentLog.log(
						Category.INFRA,
						" -> ${typeDescription.`package`}.${typeDescription.simpleName} ${typeDescription.classFileVersion} ${typeDescription.declaredTypes}"
					)

					enabledFeatures.forEach { feature ->
						feature.transform(builder, typeDescription, classLoader, javaModule, protectionDomain)
					}

					builder
				}
				.installOn(instrumentation)
		}

		internal fun getEnabledFeatures(argument: String?): Set<Feature> {
			val featureOverrides =
				argument?.split(",")?.map { it.split("=").let { it[0] to it[1] } }?.toMap() ?: mapOf()

			AgentLog.log(Category.INFRA, "Feature overrides: $featureOverrides")

			val enabledFeatures: Set<Feature> = features.filter { feature ->
				(feature.defaultEnabled && featureOverrides[feature.name] != "false")
					|| (!feature.defaultEnabled && featureOverrides[feature.name] == "true")
			}.toSet()

			AgentLog.log(Category.INFRA, "Features enabled: ${enabledFeatures.map { it.name }}")
			return enabledFeatures
		}
	}
}
