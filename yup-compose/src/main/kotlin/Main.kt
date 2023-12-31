package io.github.mathias8dev

import io.github.mathias8dev.yup.Yup
import io.github.mathias8dev.yup.constraintsListOf
import io.github.mathias8dev.yup.stateList



fun main() {
    val standardConstraints = Yup.ValidationConstraints {
        required {
            errorMessage = "This attribute is required"
        }
        minLength {
            length = 3
            errorMessage = "The minimum length should be 3"
        }

        maxLength {
            length = 10
            errorMessage = "The maximum length should be 10"
        }

        custom {
            errorMessage = "This should be exactly 6 length"
            onValidate {
                it.toString().length == 6
            }
        }
    }


    val validator = Yup.statefulValidator(Yup.reactiveValidation, Yup.preserveDateType) {
        initialStateList {
            stateList(
                "username" to "mathias8dev"
            )
        }

        constraints {
            constraintsListOf(
                "username" to standardConstraints
            )
        }
    }

    println("Hello world")

    validator.validate().getAll().forEach {
        println(it)
    }

}