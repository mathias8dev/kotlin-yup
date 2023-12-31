package io.github.mathias8dev.yup

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class Validator(
    private val validationConstraints: Map<String, ValidationConstraints>,
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

        init {
            state.setUpdateListener(this)
        }

        fun validate(): Errors {
            return super.internalValidate(state.getAll())
        }

        override fun onPreUpdate() {

        }

        override fun onPostUpdate() {
            if (useReactiveValidation) {
                validate()
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
    }

    companion object {
        internal val realRegex = Regex("[-+]?\\d*\\.?\\d+")
        internal val integerRegex = Regex("[-+]?\\d+")
        internal val passwordRegex = Regex("^\\w{8,}")
        internal val emailRegex = Regex("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?")
    }
}


