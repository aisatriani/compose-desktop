package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.model.User
import dev.burnoo.cokoin.get
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import presenter.BerandaPresenter
import presenter.UserPresenter

@Composable
@Preview
fun BerandaScreen() {
    val berandaPresenter = BerandaPresenter()
    Box(modifier = Modifier.fillMaxSize()) {
        ListUsers(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ListUsers( modifier: Modifier) {
    val bp = get<UserPresenter>()
    val userState = bp.usersState.collectAsState(UserPresenter.UserState.Loading(true))
    when(userState.value){
        is UserPresenter.UserState.Loading -> {
            val isLoading = (userState.value as UserPresenter.UserState.Loading).isLoading
           CircularProgressIndicator(modifier = modifier)
        }
        is UserPresenter.UserState.Success -> {

            LazyColumn {
                items((userState.value as UserPresenter.UserState.Success).users){
                    ItemUser(it)
                }
            }

        }
        is UserPresenter.UserState.Error -> TODO()
    }

    LaunchedEffect(true){
        bp.getUsers(false)
    }

}

@Composable
fun ItemUser(user: User) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row {



            val imageResource = lazyPainterResource(user.avatar){
                coroutineContext = Dispatchers.IO
                requestBuilder {
                    cacheControl(CacheControl.MaxAge(60 * 60))
                }
            }
            KamelImage(
                resource = imageResource,
                contentDescription = "",
                crossfade = true,
                contentScale = ContentScale.Fit,
                onLoading = {
                    Box(modifier = Modifier.size(64.dp,64.dp)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                },
                onFailure = { exception ->
//                    Snackbar {
                        Text(exception.message!!)
//                    }
                }, modifier = Modifier.padding(10.dp).size(64.dp,64.dp).clip(CircleShape))

            Column(modifier = Modifier.padding(10.dp)) {
                Text("First name : ${user.first_name}")
                Text("last name : ${user.last_name}")
                Text("Email : ${user.email}")
                Text("avatar : ${user.avatar}")
            }


        }

    }

}


@Preview
@Composable
fun PreviewItemUser(){
    ItemUser(User(1,"ais","satriani","aisatriani03@gmail.com","https://reqres.in/img/faces/2-image.jpg"))
}

@Composable
fun FormUser() {
    val bp = get<BerandaPresenter>()
    val (userState, setUsersState) = remember { mutableStateOf(User(0,"","","","")) }
    Column {
        TextField(value = userState.first_name, onValueChange = {setUsersState(userState.copy(first_name = it))}, label = { Text("Name") })
        TextField(value = userState.email, onValueChange = {setUsersState(userState.copy(email = it))}, label = { Text("Email") })
        TextField(value = userState.avatar, onValueChange = {setUsersState(userState.copy(avatar = it))}, label = { Text("Status") })
        Button(onClick = {
            bp.addUser(userState)
            setUsersState(userState.copy(first_name = "", email = "", last_name = "", avatar = ""))
        }){
            Text("Simpan")
        }
    }
}
