// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import component.ItemMenu
import dev.burnoo.cokoin.Koin
import org.koin.core.context.startKoin
import screen.BerandaScreen
import screen.LoginScreen
import screen.ProductScreen
import screen.ReportScreen


@Composable
@Preview
fun App(onLogout: () -> Unit) {

    val cont = remember {
        mutableStateOf<ContentScreen>(ContentScreen.Beranda("Beranda"))
    }

    MaterialTheme(colors = darkColors()) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(160.dp).background(Color.Gray).fillMaxHeight()) {
                ItemMenu("Beranda") {
                    cont.value = ContentScreen.Beranda("Beranda")
                }
                ItemMenu("Product") {
                    cont.value = ContentScreen.product("Product")
                }
                ItemMenu("Report") {
                    cont.value = ContentScreen.Report("Report")
                }
                ItemMenu("Logout") {
                    onLogout()
                }
            }
            Surface {
                Box(modifier = Modifier.weight(1f).fillMaxSize().padding(10.dp)) {

                    when (cont.value) {
                        is ContentScreen.Beranda -> {
                            BerandaScreen()
                        }
                        is ContentScreen.product -> {
                            ProductScreen()
                        }
                        is ContentScreen.Report -> {
                            ReportScreen()
                        }
                    }


                }

            }
        }
    }
}


fun main() = application {
    Koin(appDeclaration = { modules(di.oppModule) }){
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(width = 1024.dp, position = WindowPosition(Alignment.Center)),
            title = "Compose Desktop"
        ) {
            val uiState = remember {
                mutableStateOf<MainScreen>(MainScreen.LoginScreen)
            }
            when (uiState.value) {
                is MainScreen.LoginScreen -> {
                    LoginScreen {
                        if (it) {
                            uiState.value = MainScreen.AppScreen
                        }
                    }
                }
                is MainScreen.AppScreen -> App {
                    uiState.value = MainScreen.LoginScreen
                }
            }
        }


    }
}


sealed class ContentScreen {
    data class Beranda(val title: String) : ContentScreen()
    data class product(val title: String) : ContentScreen()
    data class Report(val title: String) : ContentScreen()
}

sealed class MainScreen {
    object AppScreen : MainScreen()
    object LoginScreen : MainScreen()
}
