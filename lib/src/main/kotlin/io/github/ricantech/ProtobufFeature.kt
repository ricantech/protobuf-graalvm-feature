package io.github.ricantech

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.ProtocolMessageEnum
import io.github.ricantech.utils.ReflectionRegistrationUtils
import org.graalvm.nativeimage.hosted.Feature
import org.graalvm.nativeimage.hosted.Feature.BeforeAnalysisAccess
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.io.InputStream
import java.util.*

class ProtobufFeature : Feature {
    override fun beforeAnalysis(access: BeforeAnalysisAccess) {
        (this::class
            .java
            .classLoader
            .getResourceAsStream("features/protobuf-packages.properties")
            ?.let { loadPackagedToScan(it) }
            ?.map { Pair(it, Reflections(it, Scanners.SubTypes)) } ?: listOf(Pair("*", Reflections())))
            .forEach { src -> registerGrpcClassesFromReflection(access, src) }
    }

    private fun loadPackagedToScan(stream: InputStream): Set<String> {
        val props = Properties()
        props.load(stream)
        return props.stringPropertyNames()
    }

    private fun registerGrpcClassesFromReflection(
        access: BeforeAnalysisAccess,
        packageNameWithReflections: Pair<String, Reflections>
    ) {
        val ref = packageNameWithReflections.second
        val toRegister = mapOf(
            ref.getSubTypesOf(GeneratedMessageV3::class.java) to ({ clazz: Class<out Any> ->
                ReflectionRegistrationUtils.registerAll(
                    access,
                    clazz.name
                )
            }),
            ref.getSubTypesOf(GeneratedMessageV3.Builder::class.java) to ({ clazz: Class<out Any> ->
                ReflectionRegistrationUtils.registerAll(
                    access,
                    clazz.name
                )
            }),
            ref.getSubTypesOf(ProtocolMessageEnum::class.java) to ({ clazz: Class<out Any> ->
                ReflectionRegistrationUtils.registerAllButInstantiation(
                    access,
                    clazz.name
                )
            })
        )
        println(
            "Registering for runtime reflection {package=${packageNameWithReflections.first}}: ${
                toRegister.flatMap { it.key }.map { it.name }
            }"
        )
        toRegister.forEach { entry ->
            entry.key.forEach { clazz ->
                entry.value.invoke(clazz)
            }
        }
    }
}
