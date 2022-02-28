package presenter

import data.repository.LoginRepository
import data.request.LoginRequest
import data.response.LoginErrorResponse
import io.ktor.client.call.*
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginPresenter(private val loginRepository: LoginRepository) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    val loginState = MutableStateFlow<LoginState>(LoginState.Initialize)

    fun login(loginRequest: LoginRequest) = launch {
        loginState.value = LoginState.Loading
        try {
            val loginResponse = loginRepository.login(loginRequest)
            println(loginResponse.token)
            loginState.value = LoginState.LoginSuccess(loginResponse.token)
        }catch (e: ClientRequestException){
            val readText = e.response.receive<LoginErrorResponse>()
            println(readText.error)
            loginState.value = LoginState.LoginFailed(readText.error)
        }
    }

    fun setInitilize() {
        loginState.value  = LoginState.Initialize
    }

    sealed class LoginState {
        data class LoginSuccess(val token: String) : LoginState()
        data class LoginFailed(val errorText: String) : LoginState()
        object Loading : LoginState()
        object Initialize : LoginState()
    }

}