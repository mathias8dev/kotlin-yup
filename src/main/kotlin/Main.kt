package io.github.mathias8dev

import io.github.mathias8dev.yup.Yup
import io.github.mathias8dev.yup.constraintsMapOf

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
    }

    standardConstraints.debug()

    val validator = Yup.statefulValidator(Yup.reactiveValidation) {
        constraintsMapOf(
            "username" to standardConstraints
        )
    }

    println("Hello world")

    validator.validate().getAll().forEach {
        println(it)
    }



}