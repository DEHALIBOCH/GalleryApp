package kz.project.domain.model.photo

data class PhotoResponse(
    val countOfPages: Int,
    val photos: MutableList<Photo>,
    val itemsPerPage: Int,
    val totalItems: Int
)