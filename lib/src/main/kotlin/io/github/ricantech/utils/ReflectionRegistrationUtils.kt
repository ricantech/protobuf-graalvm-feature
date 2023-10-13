package io.github.ricantech.utils

import org.graalvm.nativeimage.hosted.Feature
import org.graalvm.nativeimage.hosted.RuntimeReflection

object ReflectionRegistrationUtils {
    fun registerAll(access: Feature.BeforeAnalysisAccess, className: String) {
        val clazz = access.findClassByName(className)
        registerForReflectiveInstantiation(clazz)
        registerForRuntimeReflection(clazz);
        registerFieldsForRuntimeReflection(clazz);
        registerMethodsForRuntimeReflection(clazz);
        registerConstructorsForRuntimeReflection(clazz);
    }

    private fun registerForRuntimeReflection(clazz: Class<*>) {
        RuntimeReflection.register(clazz)
    }

    private fun registerForReflectiveInstantiation(clazz: Class<*>) {
        RuntimeReflection.registerForReflectiveInstantiation(clazz)
    }

    private fun registerMethodsForRuntimeReflection(clazz: Class<*>) {
        for (method in clazz.methods) {
            RuntimeReflection.register(method)
        }
    }

    private fun registerFieldsForRuntimeReflection(clazz: Class<*>) {
        for (field in clazz.fields) {
            RuntimeReflection.register(field)
        }
    }

    private fun registerConstructorsForRuntimeReflection(clazz: Class<*>) {
        for (constructor in clazz.constructors) {
            RuntimeReflection.register(constructor)
        }
    }


}