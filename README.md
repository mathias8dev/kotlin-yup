### kotlin-yup
> kotlin-yup is a type-safe, powerful and extensible fluent DSL to validate objects in Kotlin.

This version work only with compose. If you want to use it in another environments please wait a few days (I will publish a kotlin multiplatform version) or feel free to make a pull request.

[![Code Style](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](LICENSE)

## Installation

![Gradle](docs/gradle.png)

```groovy
implementation 'io.github.mathias8dev.kotlin-yup:kotlin-yup:1.0.0'
```

![Gradle](docs/gradle.png) (Kotlin DSL)

```kotlin
implementation("io.github.mathias8dev.kotlin-yup:kotlin-yup:1.0.0")
```

![Maven](docs/maven.png)

```xml
<dependency>
  <groupId>io.github.mathias8dev</groupId>
  <artifactId>kotlin-yup</artifactId>
  <version>1.0.0</version>
</dependency>
```



## Getting Started

**Stateless Validation**
<p>
  The stateless validation feature is usefully if you want to own yourself the data to validate.
  To use such feature, just use this snippet.
</p>

```kotlin

// Yup.Record is just an interface. It is used internally. There is no inherited methods or attributes
data class Employee(
  val id: Int,
  val firstname: String,
  val lastname: String,
  val email: String
): Yup.Record


val nameValidationConstraints = Yup.ValidationConstraints {
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

val validator = Yup.statelessValidator {
  constraintsListOf(
    // A validation constraints can be savec in a variable and reuse multiple time,
    "firstname" to nameValidationConstraints,
    "lastname" to nameValidationConstraints,
    // Or create on fly
    "email" to Yup.ValidationConstraints {
      required {
        errorMessage = "This field is required"
      }
      email {
        errorMessage = "This field should be a valid email"
      }
    },
    "id" to {
      required {
        errorMessage = "This field is required"
      }
      integer {
        minValue = 0
        maxValue = 100
        errorMessage = "The value of the id should be between 0 and 100"
      }
    }
  )
}

// Suppose that you have this record comming from an api or a form
val employee = Employee(0, "joel", "ana", "joel4@gmail.com")

// To validate it, just do
val errors = validator.validate(employee)

// The firstname is valid
if (errors.get("firstname").isEmpty()) {

}

// The errors object can be seen at a high level like a Map<String, List<String>>, each string in the list corresponding to a failled validation rule message

// There is no validation errors
if (errors.getAll().isEmpty()) {
  // do something
}
```

**Stateful Validation**
<p>
  The stateful validation is usefull if you don't' want to care about managing and updating the state. Yup do it for you.
</p>



```kotlin

val nameValidationConstraints = remember {
  Yup.ValidationConstraints {
    required {
      errorMessage = "This field is required"
    }
    minLength {
      length = 2
      errorMessage = "The minimum length required is 2"
    }
  }
}

val formValidator = remember {
  // Yup.reactiveValidation is an option to use if you want to enable a reactive validation
  // Yup.preserveDataType is an option to use if you have a complex date type like DateTime...
  Yup.statefulValidator(Yup.reactiveValidation, Yup.preserveDateType) {
    initialStateMap {
        mapOf(
          "firstname" to "Kossi Mathias",
          "lastname" to "KALIPE"
        )
    }
    constraints {
      constraintsListOf(
          "firstname" to nameValidationConstraints,
          "lastname" to nameValidationConstraints,
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


@Composable
fun SampleForm(
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

        // InputField is not part of kotlin-yup, you can look  at the sample folder to see our actual implementation of this custom TextField
        TextInput(
          modifier = modifier,
          value = formValidator.state.getAsString("firstname") ?: "", // formValidator.state.get return an object. So you would probably use this method if your field is something like a DateField
          placeholderText = "Type your firstname",
          hasErrors = formValidator.errors.get("firstname").isNotEmpty(), // Look if this field is error prone
          onErrorInfoClicked = {
              showErrorsDialog = true
              errorMessages = formValidator.errors.get("firstname") // Retrieve all the errorMessages in order to display it in a dialog when the use click on this button
          },
          onValueChange = {
              formValidator.state.set("firstname", it) // Update the state
          }
        )


        TextInput(
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
            enabled = formValidator.isValid, // Enable submit button if only the form is valid
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

```

### How it works

To use Yup, you just have to import Yup object from io.github.mathias8dev.yup.Yup
All of yup's api is namespaced by Yup.

In the scope of Yup.statefulValidator(...), you have access to
- initialStateMap // Here, you can like we did bellow, use stateMapOf to return your state
- initialState // Here you can just set your pojo which inherit from Yup.Record
- constraints


## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.
