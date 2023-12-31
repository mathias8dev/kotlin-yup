package io.github.mathias8dev.yup


class StateHolder internal constructor(
    private val state: MutableMap<String, Any?> = mutableMapOf()
) {
    private var updateListener: StateUpdateListener?=null

    fun get(key: String): Any? = state[key]

    fun getAsString(key: String): String? = state[key]?.toString()

    fun getAll() = state.toMap()

    fun set(key: String, value: Any?) {
        if (state.containsKey(key)) {
            updateListener?.onPreUpdate()
            state[key] = value
            updateListener?.onPostUpdate()
        }
    }

    fun set(keyPair: Pair<String, Any?>) {
        set(keyPair.first, keyPair.second)
    }

    internal fun setUpdateListener(listener: StateUpdateListener) {
        updateListener = listener
    }
}
