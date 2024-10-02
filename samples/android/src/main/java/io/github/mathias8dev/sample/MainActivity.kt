package io.github.mathias8dev.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.mathias8dev.sample.ui.composables.SampleForm
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

                    SampleForm(
                        formValidator = validator
                    )
                }


            }
        }
    }
}

@Composable
fun rememberFormValidator(): Validator.StatefulValidator {
    val nameValidatorConstraints = remember {
        Yup.ValidationConstraints {
            required {
                required = false
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
            initialStateMap {
                mapOf(
                    "firstname" to "Mathias",
                    "lastname" to "KALIPE"
                )
            }
            constraints {
                constraintsListOf(
                    "firstname" to nameValidatorConstraints,
                    "lastname" to nameValidatorConstraints,
                    "email" to Yup.ValidationConstraints {
                        required {
                            required = false
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
