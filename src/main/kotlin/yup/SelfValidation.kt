package io.github.mathias8dev.yup


internal interface SelfValidation {
    fun validate(value: String?): String?
}