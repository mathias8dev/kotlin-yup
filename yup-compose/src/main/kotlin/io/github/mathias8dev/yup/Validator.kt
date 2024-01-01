package io.github.mathias8dev.yup

import androidx.compose.runtime.mutableStateMapOf
import kotlinx.parcelize.RawValue


sealed class Validator(
    protected val validationConstraints: Map<String, ValidationConstraints>,
) {

    val errors = Errors(errors = mutableStateMapOf(*validationConstraints.map {it.key to listOf<String>() }.toTypedArray()))


    protected fun internalValidate(state: Map<String, Any?>): Errors {
        validationConstraints.forEach { (key, constraints) ->
            val stateValue = state[key]
            val errorMessages = constraints.validate(stateValue)
            this.errors.set(key, errorMessages)
        }

        return errors.copy()
    }


    class StatefulValidator(
        validationConstraints: Map<String, ValidationConstraints>,
        val state: StateHolder,
        private val useReactiveValidation: Boolean = false,
    ): Validator(
        validationConstraints = validationConstraints,
    ), StateUpdateListener {

        val isValid: Boolean
            get() {
                return validationConstraints.map { (key, constraints) ->
                    val stateValue = state.get(key)
                    constraints.validate(stateValue)
                }.flatten().isEmpty()
            }

        init {
            state.setUpdateListener(this)
        }

        fun validate(): Errors {
            return super.internalValidate(state.getAll())
        }

        private fun validate(key: String) {
            val stateValue = state.get(key)
            val errorMessages = validationConstraints[key]!!.validate(stateValue)
            this.errors.set(key, errorMessages)
        }

        override fun onPreUpdate(key: String) {

        }

        override fun onPostUpdate(key: String) {
            if (useReactiveValidation) {
                validate(key)
            }
        }
    }

    class StatelessValidator(
        validationConstraints: @RawValue Map<String, ValidationConstraints>,
    ): Validator(
        validationConstraints = validationConstraints,
    ) {
        fun validate(state: Map<String, Any?>): Errors {
            return super.internalValidate(state)
        }

        fun validate(state: Yup.Record): Errors {
            return super.internalValidate(state.toMap())
        }
    }

    companion object {
        internal val realRegex = Regex("[-+]?\\d*\\.?\\d+")
        internal val integerRegex = Regex("[-+]?\\d+")
        internal val passwordRegex = Regex("^\\w{8,}")
        internal val emailRegex = Regex("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?")
    }
}


