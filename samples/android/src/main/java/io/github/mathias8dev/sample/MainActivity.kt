package io.github.mathias8dev.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mathias8dev.sample.ui.composables.FormDialog
import io.github.mathias8dev.sample.ui.composables.InputField
import io.github.mathias8dev.sample.ui.theme.SampleTheme
import io.github.mathias8dev.yup.Validator
import io.github.mathias8dev.yup.Yup
import io.github.mathias8dev.yup.constraintsListOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val validator = rememberFormValidator()

                    FormSample(
                        formValidator = validator
                    )
                }


            }
        }
    }
}

@Composable
fun rememberFormValidator(): Validator.StatefulValidator {
    val nameValidator = remember {
        Yup.ValidationConstraints {
            required {
                errorMessage = "This field is required"
            }
            minLength {
                length = 2
                errorMessage = "The minimum length required is 2"
            }
            maxLength {
                length = 20
                errorMessage = "The maximum length required is 20"
            }
        }
    }
    return remember {
        Yup.statefulValidator(Yup.reactiveValidation, Yup.preserveDateType) {
            initialStateList {
                mapOf(
                    "firstname" to "Mathias",
                    "lastname" to "KALIPE"
                )
            }
            constraints {
                constraintsListOf(
                    "firstname" to nameValidator,
                    "lastname" to nameValidator,
                    "email" to Yup.ValidationConstraints {
                        required {
                            errorMessage = "The email address is required"
                        }

                        email {
                            use = true
                            errorMessage = "This email address is invalid"
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun FormSample(
    formValidator: Validator.StatefulValidator
) {

    var showErrorsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var errorMessages by remember {
        mutableStateOf(emptyList<String>())
    }

    val modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)

    val context = LocalContext.current

    Column {

        InputField(
            modifier = modifier,
            value = formValidator.state.getAsString("firstname") ?: "",
            placeholderText = "Type your firstname",
            hasErrors = formValidator.errors.get("firstname").isNotEmpty(),
            onErrorInfoClicked = {
                showErrorsDialog = true
                errorMessages = formValidator.errors.get("firstname")
            },
            onValueChange = {
                formValidator.state.set("firstname", it)
            }
        )


        InputField(
            modifier = modifier,
            value = formValidator.state.getAsString("lastname") ?: "",
            placeholderText = "Type your lastname",
            hasErrors = formValidator.errors.get("lastname").isNotEmpty(),
            onErrorInfoClicked = {
                showErrorsDialog = true
                errorMessages = formValidator.errors.get("lastname")
            },
            onValueChange = {
                formValidator.state.set("lastname", it)
            }
        )

        InputField(
            modifier = modifier,
            value = formValidator.state.getAsString("email") ?: "",
            placeholderText = "Type your email address",
            hasErrors = formValidator.errors.get("email").isNotEmpty(),
            onErrorInfoClicked = {
                showErrorsDialog = true
                errorMessages = formValidator.errors.get("email")
            },
            onValueChange = {
                formValidator.state.set("email", it)
            }
        )

        Button(
            modifier = modifier.height(48.dp),
            enabled = formValidator.isValid,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                Toast.makeText(
                    context,
                    "Your form is submitted successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            Text(text = "Submit")
        }

    }

    if (showErrorsDialog && errorMessages.isNotEmpty()) {
        FormDialog(
            onDismissRequest = {
                showErrorsDialog = false
            }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.Red.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )

            errorMessages.forEach {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}