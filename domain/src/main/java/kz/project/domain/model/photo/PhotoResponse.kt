package kz.project.domain.model.photo

data class PhotoResponse(
    val countOfPages: Int,
    val photos: List<Photo>,
    val itemsPerPage: Int,
    val totalItems: Int
)