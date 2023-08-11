package kz.project.data.remote.dto.photo


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)