package io.github.mathias8dev.sample.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    value: String,
    placeholderText: String,
    hasErrors: Boolean,
    onErrorInfoClicked: ()->Unit,
    onValueChange: (updatedValue: String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.Black
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Person,
                tint = Color.Black,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (hasErrors) {
                IconButton(onClick = onErrorInfoClicked) {
                    Icon(
                        Icons.Filled.Info,
                        tint = Color.Red,
                        contentDescription = null
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(

        ),
        singleLine = true,
        modifier = modifier
    )
}