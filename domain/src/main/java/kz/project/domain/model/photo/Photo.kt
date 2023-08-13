package kz.project.domain.model.photo

data class Photo(
    val dateCreate: String,
    val description: String,
    val id: Int,
    val image: Image,
    val name: String,
    val new: Boolean,
    val popular: Boolean,
    val user: String
)