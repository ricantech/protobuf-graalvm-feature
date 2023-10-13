package io.github.ricantech

import com.google.protobuf.GeneratedMessageV3
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
            ?.map { Reflections(it, Scanners.SubTypes) } ?: listOf(Reflections()))
            .forEach { src -> registerGrpcClassesFromReflection(access, src) }
    }

    private fun loadPackagedToScan(stream: InputStream): Set<String> {
        val props = Properties()
        props.load(stream)
        return props.stringPropertyNames()
    }

    private fun registerGrpcClassesFromReflection(access: BeforeAnalysisAccess, ref: Reflections) {
        (ref.getSubTypesOf(GeneratedMessageV3::class.java) +
                ref.getSubTypesOf(GeneratedMessageV3.Builder::class.java))
            .forEach {
                println("Registering for runtime reflection: ${it.name}")
                ReflectionRegistrationUtils.registerAll(access, it.name)
            }
    }
}
