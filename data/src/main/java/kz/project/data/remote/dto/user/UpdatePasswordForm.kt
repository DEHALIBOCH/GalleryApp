package kz.project.data.remote.dto.user


import com.google.gson.annotations.SerializedName

data class UpdatePasswordForm(
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("oldPassword")
    val oldPassword: String,
)