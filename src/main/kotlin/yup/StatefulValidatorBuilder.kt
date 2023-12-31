package io.github.mathias8dev.yup


class StatefulValidatorBuilder internal constructor(
    private val options: Array<out Yup.Options>
) {

    private var initialState = mapOf<String, String>()
    private lateinit var validationConstraints: Map<String, ValidationConstraints>

    fun initialState(
        builder: () -> Map<String, String>
    ) {
        initialState = builder()
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
            state = StateHolder(state = mutableMapOf(*validationConstraints.map { it.key to (initialState[it.key] ?: "") }
                .toTypedArray()))
        )
    }
}