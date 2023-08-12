package kz.project.domain.model.photo

data class Image(
    val id: Int,
    val name: String
)

fun createEmptyImage() = Image(
    id = -1,
    name = "",
)