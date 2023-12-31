package io.github.mathias8dev.sample.ui.composables

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier


@Stable
fun Modifier.useModifierIf(
    condition: Boolean,
    callback: (currentModifier: Modifier) -> Modifier
): Modifier {
    return if (condition) callback(this) else this
}