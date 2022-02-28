package data.response

@kotlinx.serialization.Serializable
class SingleResponse<T> {
    val data : T? = null
}
