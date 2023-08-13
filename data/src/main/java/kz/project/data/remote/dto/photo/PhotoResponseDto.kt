package kz.project.data.remote.dto.photo


import com.google.gson.annotations.SerializedName

data class PhotoResponseDto(
    @SerializedName("countOfPages")
    val countOfPages: Int?,
    @SerializedName("data")
    val photos: List<PhotoDto>?,
    @SerializedName("itemsPerPage")
    val itemsPerPage: Int?,
    @SerializedName("totalItems")
    val totalItems: Int?
)