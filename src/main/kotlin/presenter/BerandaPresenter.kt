package presenter

import androidx.compose.runtime.mutableStateListOf
import data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

class BerandaPresenter : CoroutineScope {
    private val job = SupervisorJob() + Dispatchers.IO
    val mutex = Mutex()
    val dataUsers = MutableStateFlow(emptyList<User>())
    private val _users = mutableStateListOf<User>()
    fun addUser(user: User) {
        launch {
            mutex.withLock {
                _users.add(user)
                dataUsers.value = _users
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job
}