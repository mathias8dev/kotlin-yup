package io.github.mathias8dev.yup

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Stable
@Parcelize
data class Errors(
    private val errors: @RawValue SnapshotStateMap<String, List<String>> = mutableStateMapOf()
) : Parcelable {
    fun get(key: String): List<String> = errors[key] ?: emptyList()

    fun getAll(): List<String> = errors.values.flatten()

    fun entries() = errors.toMap().entries

    fun isEmpty(): Boolean = getAll().isEmpty()

    internal fun set(key: String, value: List<String>) {
        if (errors.containsKey(key)) errors[key] = value
    }

    internal fun set(keyPair: Pair<String, List<String>>) {
        set(keyPair.first, keyPair.second)
    }
}
