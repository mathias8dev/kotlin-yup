package io.github.mathias8dev.yup

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Stable
@Parcelize
class StateHolder internal constructor(
    private val state: @RawValue SnapshotStateMap<String, Any?> = mutableStateMapOf()
) : Parcelable {
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
