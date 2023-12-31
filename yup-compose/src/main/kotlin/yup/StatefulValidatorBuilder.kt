package io.github.mathias8dev.yup

import androidx.compose.runtime.mutableStateMapOf


class StatefulValidatorBuilder internal constructor(
    private val options: Array<out Yup.Options>
) {

    private var initialState = mapOf<String, Any?>()
    private lateinit var validationConstraints: Map<String, ValidationConstraints>

    fun initialStateList(
        builder: () -> Map<String, Any?>
    ) {
        initialState = builder()
    }

    fun initialState(
        builder: () -> Yup.Record
    ) {
        val record = builder()
        initialState = if (options.contains(Yup.Options.PRESERVE_TYPE))
            record.toMap()
        else record.toStringMap()
    }


    fun constraints(
        builder: ValidationConstraints.Builder.() -> Map<String, ValidationConstraints>
    ) {
        val constraintsBuilder = ValidationConstraints.Builder()
        validationConstraints = builder(constraintsBuilder)

    }

    internal fun build(): Validator.StatefulValidator {
        return Validator.StatefulValidator(
            useReactiveValidation = options.contains(Yup.Options.REACTIVE),
            validationConstraints = validationConstraints.toMap(),
            state = StateHolder(
                state = mutableStateMapOf(*validationConstraints.map {
                    it.key to initialState[it.key]
                }.toTypedArray())
            )
        )
    }
}