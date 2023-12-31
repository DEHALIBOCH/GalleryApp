package kz.project.data.remote.dto.photo


import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("dateCreate")
    val dateCreate: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: ImageDto?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("new")
    val new: Boolean?,
    @SerializedName("popular")
    val popular: Boolean?,
    @SerializedName("user")
    val user: String?
)