package io.github.mathias8dev.yup


internal interface StateUpdateListener {
    fun onPreUpdate()
    fun onPostUpdate()
}