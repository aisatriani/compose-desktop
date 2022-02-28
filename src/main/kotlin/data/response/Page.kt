package data.response

@kotlinx.serialization.Serializable
class Page<T> {
    var page = 0
    var per_page = 0
    var total = 0
    var total_pages = 0
    var data : List<T> = mutableListOf()
}