package io.github.mathias8dev.yup


data class ValidationConstraints internal constructor(
    private val constraints: MutableList<ValidationConstraint> = mutableListOf()
) {

    fun validate(value: String?): List<String> {
        val errors = mutableListOf<String>()
        constraints.forEach {
            it.validate(value)?.let { errorMessage -> errors.add(errorMessage) }
        }

        return errors
    }

    fun debug() {
        println("[debug] - ValidationConstraints")
        constraints.forEach {
            println("ValidationConstraint - $it")
        }
    }

    fun add(constraint: ValidationConstraint) {
        constraints.add(constraint)
    }

    class Builder internal constructor() {
        private lateinit var minLengthConstraint: ValidationConstraint.MinLength
        private lateinit var maxLengthConstraint: ValidationConstraint.MaxLength
        private lateinit var requiredConstraint: ValidationConstraint.Required
        private lateinit var passwordConstraint: ValidationConstraint.Password
        private lateinit var emailConstraint: ValidationConstraint.Email
        private lateinit var integerConstraint: ValidationConstraint.Integer
        private lateinit var realConstraint: ValidationConstraint.Real
        private lateinit var stringConstraint: ValidationConstraint.String
        private lateinit var regexConstraint: ValidationConstraint.Regex

        fun minLength(builder: ValidationConstraint.MinLength.() -> Unit) {
            if (!::minLengthConstraint.isInitialized) {
                minLengthConstraint = ValidationConstraint.MinLength(Yup.DEFAULT_MIN_LENGTH)
            }
            builder(minLengthConstraint)
            println("MinLengthConstraintBuilder method called")
            println("MinLengthConstraint is MinLength(length=${minLengthConstraint.length}, errorMessage=${minLengthConstraint.errorMessage})")
        }

        fun maxLength(builder: ValidationConstraint.MaxLength.() -> Unit) {
            if (!::maxLengthConstraint.isInitialized) {
                maxLengthConstraint = ValidationConstraint.MaxLength(Yup.DEFAULT_MAX_LENGTH)
            }
            builder(maxLengthConstraint)
            println("MaxLengthConstraintBuilder method called")
            println("MaxLengthConstraint is MaxLength(length=${maxLengthConstraint.length}, errorMessage=${maxLengthConstraint.errorMessage})")
        }

        fun required(builder: (ValidationConstraint.Required.() -> Unit)? = null) {
            if (!::requiredConstraint.isInitialized) {
                requiredConstraint = ValidationConstraint.Required()
            }
            requiredConstraint.required = true
            builder?.invoke(requiredConstraint)
            println("RequiredConstraintBuilder method called")
            println("RequiredConstraint is RequiredConstraint(required=${requiredConstraint.required}, errorMessage=${requiredConstraint.errorMessage})")
        }

        fun integer(builder: (ValidationConstraint.Integer.() -> Unit)? = null) {
            if (!::integerConstraint.isInitialized) {
                integerConstraint = ValidationConstraint.Integer()
            }
            integerConstraint.use = true
            builder?.invoke(integerConstraint)
        }

        fun real(builder: (ValidationConstraint.Real.() -> Unit)? = null) {
            if (!::realConstraint.isInitialized) {
                realConstraint = ValidationConstraint.Real()
            }
            realConstraint.use = true
            builder?.invoke(realConstraint) ?: { realConstraint.use = true }
        }

        fun string(builder: (ValidationConstraint.String.() -> Unit)? = null) {
            if (!::stringConstraint.isInitialized) {
                stringConstraint = ValidationConstraint.String()
            }
            stringConstraint.use = true
            builder?.invoke(stringConstraint)
        }

        fun email(builder: (ValidationConstraint.Email.() -> Unit)? = null) {
            if (!::emailConstraint.isInitialized) {
                emailConstraint = ValidationConstraint.Email()
            }
            emailConstraint.use = true
            builder?.invoke(emailConstraint)
        }

        fun password(builder: (ValidationConstraint.Password.() -> Unit)? = null) {
            if (!::passwordConstraint.isInitialized) {
                passwordConstraint = ValidationConstraint.Password()
            }
            passwordConstraint.use = true
            builder?.invoke(passwordConstraint)
        }

        fun regex(builder: ValidationConstraint.Regex.() -> Unit) {
            if (!::regexConstraint.isInitialized) {
                regexConstraint = ValidationConstraint.Regex()
            }
            builder(regexConstraint)
        }

        internal fun build() = ValidationConstraints().apply {
            if (::minLengthConstraint.isInitialized) {
                add(minLengthConstraint.copy())
            }
            if (::maxLengthConstraint.isInitialized) {
                add(maxLengthConstraint.copy())
            }

            if (::requiredConstraint.isInitialized) {
                add(requiredConstraint.copy())
            }

            if (::integerConstraint.isInitialized) {
                add(integerConstraint.copy())
            }

            if (::stringConstraint.isInitialized) {
                add(stringConstraint.copy())
            }

            if (::realConstraint.isInitialized) {
                add(realConstraint.copy())
            }

            if (::regexConstraint.isInitialized) {
                add(regexConstraint.copy())
            }

            if (::passwordConstraint.isInitialized) {
                add(passwordConstraint.copy())
            }

            if (::emailConstraint.isInitialized) {
                add(emailConstraint.copy())
            }
        }


    }

}