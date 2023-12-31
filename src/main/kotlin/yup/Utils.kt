package io.github.mathias8dev.yup

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


internal fun Yup.Record.toMap(): Map<String, Any?> {
    return (this::class as KClass<Yup.Record>).memberProperties.associate { prop ->
        prop.name to prop.get(this)
    }
}

internal fun Yup.Record.toStringMap(): Map<String, String?> {
    return (this::class as KClass<Yup.Record>).memberProperties.associate { prop ->
        prop.name to prop.get(this)?.toString()
    }
}


fun constraintsListOf(vararg args: Pair<String, ValidationConstraints>): Map<String, ValidationConstraints> {
    return mapOf(*args)
}

fun stateList(vararg args: Pair<String, String?>): Map<String, String?> {
    return mapOf(*args)
}