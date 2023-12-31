package io.github.mathias8dev.yup


object Yup {

    private val constraintsBuilder = ValidationConstraints.Builder()

    fun statefulValidator(
        vararg options: Options,
        builder: ValidationConstraints.Builder.() -> Map<String, ValidationConstraints>
    ): Validator.StatefulValidator {
        val validationConstraints = builder(constraintsBuilder)

        return Validator.StatefulValidator(
            useReactiveValidation = options.contains(Options.REACTIVE),
            validationConstraints = validationConstraints.toMap(),
            state = StateHolder(state = mutableMapOf(*validationConstraints.map { it.key to "" }.toTypedArray()))
        )
    }

    fun statelessValidator(builder: ValidationConstraints.Builder.() -> List<Pair<String, ValidationConstraints>>): Validator.StatelessValidator {
        val validationConstraints = builder(constraintsBuilder)
        return Validator.StatelessValidator(
            validationConstraints = validationConstraints.toMap(),
        )
    }


    fun ValidationConstraints(builder: ValidationConstraints.Builder.() -> Unit): ValidationConstraints {
        builder(constraintsBuilder)
        return constraintsBuilder.build()
    }

    val reactiveValidation = Options.REACTIVE
    internal const val DEFAULT_MIN_LENGTH = 1
    internal const val DEFAULT_MAX_LENGTH = 8

    enum class Options {
        REACTIVE
    }


    internal interface StateUpdateListener {
        fun onPreUpdate()
        fun onPostUpdate()
    }


}

fun constraintsMapOf(vararg pairs: Pair<String, ValidationConstraints>): Map<String, ValidationConstraints> {
    return mapOf(*pairs)
}
