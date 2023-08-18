package kz.project.data.remote.dto.error.error_model


import com.google.gson.annotations.SerializedName

data class UpdatePasswordErrorDto(
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("title")
    val title: String?,
)