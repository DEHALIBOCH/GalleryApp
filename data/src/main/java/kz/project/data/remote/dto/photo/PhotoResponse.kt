package kz.project.data.remote.dto.photo


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("countOfPages")
    val countOfPages: Int?,
    @SerializedName("data")
    val photos: List<Photo>?,
    @SerializedName("itemsPerPage")
    val itemsPerPage: Int?,
    @SerializedName("totalItems")
    val totalItems: Int?
)