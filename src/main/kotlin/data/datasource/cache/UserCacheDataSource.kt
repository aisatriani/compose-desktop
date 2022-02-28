package data.datasource.cache

import data.model.User
import exception.UserValidationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.regex.Pattern

class UserCacheDataSource {
    private val mutex = Mutex()
    private val users = mutableListOf<User>()
    suspend fun getUsersData(): List<User> {
        return users
    }

    suspend fun getSingleUser(id: Int) : User? {
        return users.firstOrNull { it.id == id }
    }

    /**
     * add user
     * This function thrown with UserValidationException
     */
    suspend fun addUser(user: User){
        validateUserInput(user)
        mutex.withLock {
            users.add(user)
        }
    }

    private fun validateUserInput(user: User) {
        if (user.id <= 0) {
            throw UserValidationException("id cannot be zero")
        }
        if (user.first_name.isEmpty()) {
            throw UserValidationException("first name cannot be empty")
        }
        if (user.last_name.isEmpty()) {
            throw UserValidationException("last name cannot be empty")
        }
        if (user.email.isEmpty()) {
            throw UserValidationException("email cannot be empty")
        }
        if (!checkEmail(user.email)) {
            throw UserValidationException("invalid format email address")
        }
    }

    suspend fun deleteUser(id: Int) {
        mutex.withLock {
            try {

                val index = users.indexOfFirst { it.id == id }
                if(index != -1)
                    users.removeAt(index)

            }catch (e : IndexOutOfBoundsException){
                e.printStackTrace()
            }

        }
    }

    suspend fun addAllUser(data: List<User>) {
        mutex.withLock {
            users.addAll(data)
        }
    }

    fun checkEmail(email :String) : Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

}