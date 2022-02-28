package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.request.LoginRequest
import dev.burnoo.cokoin.get
import presenter.LoginPresenter

@Composable
@Preview
fun PreviewLoginScreen() {
    LoginScreen {

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(onLogin: (Boolean) -> Unit) {
    val loginPresenter = get<LoginPresenter>()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = FocusRequester()
    val stateLogin = loginPresenter.loginState.collectAsState(LoginPresenter.LoginState.Initialize)

    if(stateLogin.value is LoginPresenter.LoginState.LoginSuccess){
        onLogin(true)
    }

    MaterialTheme(colors = darkColors()) {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center).width(300.dp)) {
                    Text(
                        text = "Compose Desktop",
                        fontSize = 25.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (stateLogin.value is LoginPresenter.LoginState.LoginFailed) {
                        Text(
                            "Invalid username or password",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = username.value,
                        onValueChange = {
                            username.value = it
                            loginPresenter.setInitilize()
                        },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester = focusRequester),
                        placeholder = { Text("username") },
                        label = { Text("username") },
                        textStyle = TextStyle(fontSize = 18.sp),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                            loginPresenter.setInitilize()
                        },
                        modifier = Modifier.fillMaxWidth().onPreviewKeyEvent {
                            if (it.key == Key.Enter && it.type == KeyEventType.KeyUp) {
                                println("enter click")
//                                loginAction(username, password, onLogin, stateLogin)
                                loginPresenter.login(LoginRequest(username.value,password.value))
                                true
                            } else
                                false
                        },
                        placeholder = { Text("password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {
                         loginPresenter.login(LoginRequest(username.value,password.value))
                    }, modifier = Modifier.fillMaxWidth(), interactionSource = interactionSource) {
                        if(stateLogin.value is LoginPresenter.LoginState.Loading){
                            CircularProgressIndicator(modifier = Modifier.size(21.dp), color = Color.Red)
                        }else{
                            Text("LOGIN")
                        }
                    }
                }
            }

        }

    }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
}

fun loginAction(
    username: MutableState<String>,
    password: MutableState<String>,
    onLogin: (Boolean) -> Unit,
    stateLogin: MutableState<Boolean>
) {
    if (username.value == "aisatriani" && password.value == "handule") {
        onLogin(true)
        stateLogin.value = true
    } else {
        onLogin(false)
        stateLogin.value = false
    }
}