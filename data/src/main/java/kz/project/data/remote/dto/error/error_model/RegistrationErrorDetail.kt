package kz.project.data.remote.dto.error.error_model


import com.google.gson.annotations.SerializedName

/**
 * Возникает при ошибке регистрации, например email уже был использован
 */
data class RegistrationErrorDetail(
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("violations")
    val violations: List<Violation?>?
)