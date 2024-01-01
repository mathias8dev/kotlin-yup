package io.github.mathias8dev.yup


sealed class ValidationConstraint(
    open var errorMessage: kotlin.String? = null,
) {

    internal abstract fun validate(value: Any?): kotlin.String?

    data class MinLength(
        var length: Int,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            println("MinLength: The error message is $errorMessage")
            println("MinLength: validate called with ${value}")
            val message = value?.toString()?.let {
                if (it.length < length) errorMessage else null
            }
            println("MinLength: The error message is $message")
            return message
        }
    }

    data class MaxLength(
        var length: Int,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            println("MaxLength: validate called with $value")
            return value?.toString()?.let {
                if (it.length > length) errorMessage else null
            }
        }
    }

    data class Required(
        var required: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            println("Required: validate called with $value")
            return if (value?.toString().isNullOrEmpty()) {
                errorMessage
            }else {
                null
            }
        }
    }

    data class Integer(
        var use: Boolean = false,
        var minValue: Int? = null,
        var maxValue: Int? = null,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (use && value?.toString()?.matches(Validator.integerRegex) == false)
                errorMessage
            else {
                val intValue = value.toString().toInt()
                if ((minValue != null && minValue!! > intValue) ||
                    (maxValue != null && maxValue!! < intValue)
                )
                    errorMessage
                else null
            }

        }
    }

    data class Real(
        var use: Boolean = false,
        var minValue: Float? = null,
        var maxValue: Float? = null,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (use && value?.toString()?.matches(Validator.realRegex) == false)
                errorMessage
            else{
                val floatValue = value.toString().toFloat()
                if ((minValue != null && minValue!! > floatValue) ||
                    (maxValue != null && maxValue!! < floatValue)
                )
                    errorMessage
                else null
            }
        }
    }

    data class String(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (use && value?.toString().isNullOrBlank()) errorMessage else null
        }
    }

    data class Email(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (use && value?.toString()?.matches(Validator.emailRegex) == false)
                errorMessage
            else null
        }
    }

    data class Password(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (use && value?.toString()?.matches(Validator.passwordRegex) == false)
                errorMessage
            else null
        }
    }

    data class Regex(
        var regex: kotlin.text.Regex? = null,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return if (regex != null && value?.toString()?.matches(regex!!) == false)
                errorMessage
            else null
        }
    }


    data class Custom(
        var onValidate: CustomConstraintResult? = null,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: Any?): kotlin.String? {
            return onValidate?.let {
                if (!it.onValidate(value))
                    errorMessage
                else null
            }
        }

        fun liteCopy(): Custom {
            return Custom(
                onValidate = onValidate,
                errorMessage = errorMessage
            )
        }

        fun onValidate(callback: (value: Any?) -> Boolean): CustomConstraintResult {
            return object : CustomConstraintResult {
                override fun onValidate(value: Any?): Boolean {
                    return callback(value)
                }

            }
        }
    }

}

interface CustomConstraintResult {

    fun onValidate(value: Any?): Boolean
}


