package presenter

import data.model.User
import data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

class UserPresenter (private val userRepository: UserRepository) : CoroutineScope, KoinComponent {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    val usersState = MutableStateFlow<UserState>(UserState.Loading(true))
//    val users = MutableStateFlow(emptyList<User>())

    fun getUsers(reload: Boolean) = launch {
        usersState.value = UserState.Loading(true)
        val usersList = userRepository.getUsers(reload)
        usersState.value = UserState.Success(usersList)
    }

    sealed class UserState {
        data class Success(var users: List<User>) : UserState()
        data class Error(var exception: Exception) : UserState()
        data class Loading(var isLoading: Boolean) : UserState()
    }

}