package io.github.mathias8dev.yup


sealed class ValidationConstraint(
    open var errorMessage: kotlin.String? = null,
) : SelfValidation {
    data class MinLength(
        var length: Int,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            println("MinLength: The error message is $errorMessage")
            println("MinLength: validate called with ${value==null}")
            val message =  value?.let {
                if (it.length < length) errorMessage else null
            } ?: errorMessage
            println("MinLength: The error message is $message")
            return message
        }
    }

    data class MaxLength(
        var length: Int,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            println("MaxLength: validate called with $value")
            return value?.let {
                if (it.length > length) errorMessage else null
            } ?: errorMessage
        }
    }

    data class Required(
        var required: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            println("Required: validate called with $value")
            return if (value.isNullOrEmpty()) errorMessage else null
        }
    }

    data class Integer(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (use && value?.matches(Validator.integerRegex) == false)
                errorMessage
            else null
        }
    }

    data class Real(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (use && value?.matches(Validator.realRegex) == false)
                errorMessage
            else null
        }
    }

    data class String(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (use && value.isNullOrBlank()) errorMessage else null
        }
    }

    data class Email(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (use && value?.matches(Validator.emailRegex) == false)
                errorMessage
            else null
        }
    }

    data class Password(
        var use: Boolean = false,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (use && value?.matches(Validator.passwordRegex) == false)
                errorMessage
            else null
        }
    }

    data class Regex(
        var regex: kotlin.text.Regex? = null,
        override var errorMessage: kotlin.String? = null
    ) : ValidationConstraint(errorMessage = errorMessage) {
        override fun validate(value: kotlin.String?): kotlin.String? {
            return if (regex != null && value?.matches(regex!!) == false)
                errorMessage
            else null
        }
    }

}


