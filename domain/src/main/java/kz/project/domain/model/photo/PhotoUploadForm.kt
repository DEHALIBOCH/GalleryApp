package kz.project.domain.model.photo

/**
 * images - это не объект images, это - путь "api/media_objects/{id}"
 */
data class PhotoUploadForm(
    val name: String,
    val dateCreate: String,
    val description: String,
    val new: Boolean,
    val popular: Boolean,
    val image: String = "",
)