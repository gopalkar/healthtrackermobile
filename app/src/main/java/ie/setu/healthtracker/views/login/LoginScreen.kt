package ie.setu.healthtracker.views.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.relay.compose.BoxScopeInstance.boxAlign
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.userimagelogo.UserImageLogo
import ie.setu.healthtracker.userimagelogo.UserImageLogoIcon
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(firebaseHelper: FirebaseHelper, onSuccess : () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val appName = context.getString(R.string.app_name)
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

/*        MaterialTheme {
            RelayContainer {
                UserImageLogoIcon(modifier = Modifier
                    .width(107.dp)
                    .height(107.dp))
            }
        }*/
        Image(
            painter = painterResource(R.drawable.image_logo),
            contentDescription = "Placeholder Image"
        )

        Text(
            text = appName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move focus to the password field
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    val icon = if (passwordVisible) {
                        painterResource(id = R.drawable.ic_hide_password)
                    } else {
                        painterResource(id = R.drawable.ic_show_password)
                    }
                    Icon(
                        painter = icon,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color.DarkGray
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (validateInput(email, password)) {
                        coroutineScope.launch {
                            //val isRegistered = checkIfEmailRegistered(email)
                            //Timber.i("Registered??: $isRegistered")
                            //if (isRegistered) {
                            firebaseHelper.performLogin(email, password, onSuccess) { error ->
                                errorMessage = error // Set error message if authentication fails
                            }
                        }
                    } else {
                        errorMessage = "Please enter valid email and password"
                    }
                },
                modifier = Modifier.width(131.dp)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (validateInput(email, password)) {
                        coroutineScope.launch {
                            firebaseHelper.performSignup(email, password, onSuccess) { error ->
                                errorMessage = error // Set error message if authentication fails
                            }
                        }
                    } else {
                        errorMessage = "Please enter valid email and password"
                    }
                },
                modifier = Modifier.width(131.dp)
            ) {
                Text("Signup")
            }
        }

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

    }
}

fun validateInput(email: String, password: String): Boolean {
    return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotBlank()
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(firebaseHelper = viewModel(), onSuccess = {})
}