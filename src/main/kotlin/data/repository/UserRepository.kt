package data.repository

import data.datasource.UserDataSource
import data.datasource.cache.UserCacheDataSource
import data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

class UserRepository(private val userCacheDataSource: UserCacheDataSource, private val userRemoteDataSource: UserDataSource) : KoinComponent {

    suspend fun getUsers(reload: Boolean) : List<User> {
        if(reload || userCacheDataSource.getUsersData().isEmpty()){

            val data = userRemoteDataSource.getUsers().data
            userCacheDataSource.addAllUser(data)
            return data
        }

        return userCacheDataSource.getUsersData()
    }

    suspend fun getUser(id: Int, reload: Boolean) : User? {
        if(reload || userCacheDataSource.getSingleUser(id) == null){
            val data = userRemoteDataSource.getUser(id).data
            if (data != null) {
                userCacheDataSource.addUser(data)
            }
            return data
        }

        return userCacheDataSource.getSingleUser(id)
    }

}