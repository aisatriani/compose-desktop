package data.datasource.cache

import data.datasource.UserDataSource
import data.model.User
import data.response.Page
import data.response.SingleResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserCacheDataSource {
    private val mutex = Mutex()
    private val users = mutableListOf<User>()
    suspend fun getUsersData(): List<User> {
        return users
    }

    suspend fun getSingleUser(id: Int) : User? {
        return users.filter { it.id == id }.firstOrNull()
    }

    suspend fun addUser(user: User){
        mutex.withLock {
            users.add(user)
        }
    }

    suspend fun deleteUser(id: Int) {
        mutex.withLock {
            val index = users.indexOfFirst { it.id == id }
            users.removeAt(index)
        }
    }

    suspend fun addAllUser(data: List<User>) {
        mutex.withLock {
            users.addAll(data)
        }
    }

}