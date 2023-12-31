package io.github.mathias8dev.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.mathias8dev.sample.ui.theme.SampleTheme
import io.github.mathias8dev.yup.Validator
import io.github.mathias8dev.yup.Yup
import io.github.mathias8dev.yup.constraintsListOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
                val validator = rememberFormValidator()

                FormSample(formValidator = validator)

            }
        }
    }
}

@Composable
fun rememberFormValidator(): Validator.StatefulValidator {
    return remember {
        Yup.statefulValidator(Yup.reactiveValidation, Yup.preserveDateType) {
            constraints {
                constraintsListOf(
                    "age" to Yup.ValidationConstraints {
                        required {
                            errorMessage = "This field is required"
                        }
                        integer {
                            minValue = 18
                            maxValue = 60
                            errorMessage = "Your old should be between 18 and 60"
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

    Column {
        TextField(
            value = formValidator.state.getAsString("age") ?: "",
            onValueChange = {
                formValidator.state.set("age", it)
            }
        )

        formValidator.errors.entries().forEach {
            it.value.forEach {errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleTheme {
        Greeting("Android")
    }
}