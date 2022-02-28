import data.datasource.cache.UserCacheDataSource
import data.model.User
import exception.UserValidationException
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class UserCacheDataSourceTest {

    lateinit var userCacheDataSource: UserCacheDataSource

    @BeforeTest
    fun setup(){
        userCacheDataSource = UserCacheDataSource()
    }

    @Test
    fun testGetAllUserSuccess()= runBlocking {
        userCacheDataSource.addUser(User(
            1,"ais","satriani","aisatriani@gmail.com",""
        ))
        assertEquals(1,userCacheDataSource.getUsersData().size)
        val singleUser = userCacheDataSource.getSingleUser(1)
        assertEquals("ais",singleUser?.first_name)
        userCacheDataSource.deleteUser(1)
        assertEquals(0,userCacheDataSource.getUsersData().size)
    }

    @Test
    fun testValidateAddUser(): Unit = runBlocking {
        val idex = assertFailsWith(UserValidationException::class){
            userCacheDataSource.addUser(User(-1,"","","",""))
        }
        assertEquals("id cannot be zero",idex.message)
        val fnameEx = assertFailsWith(UserValidationException::class){
            userCacheDataSource.addUser(User(1,"fefsf","sefsef","werqer",""))
        }
        assertEquals("invalid format email address",fnameEx.message)
    }

}