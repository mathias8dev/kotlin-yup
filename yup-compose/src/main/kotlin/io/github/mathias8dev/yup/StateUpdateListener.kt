package io.github.mathias8dev.yup


internal interface StateUpdateListener {
    fun onPreUpdate(key: String)
    fun onPostUpdate(key: String)
}