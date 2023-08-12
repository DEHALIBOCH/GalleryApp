package kz.project.data.remote.dto.photo

data class PhotoUploadForm(
    val dateCreate: String,
    val description: String,
    val name: String,
    val new: Boolean? = null,
    val popular: Boolean? = null,
    val image: Image
)