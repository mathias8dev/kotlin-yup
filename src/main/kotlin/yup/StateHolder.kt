package io.github.mathias8dev.yup


class StateHolder internal constructor(
    private val state: MutableMap<String, String> = mutableMapOf()
) {
    private var updateListener: StateUpdateListener?=null

    fun get(key: String): String? = state[key]

    fun getAll() = state.toMap()

    fun set(key: String, value: String) {
        if (state.containsKey(key)) {
            updateListener?.onPreUpdate()
            state[key] = value
            updateListener?.onPostUpdate()
        }
    }

    fun set(keyPair: Pair<String, String>) {
        set(keyPair.first, keyPair.second)
    }

    internal fun setUpdateListener(listener: StateUpdateListener) {
        updateListener = listener
    }
}
