package hello.upload.domain

data class Item(

    var id: Long? = null,
    val itemName: String? = null,
    val attachFile: UploadFile? = null,
    val imageFiles: List<UploadFile>? = mutableListOf()
)