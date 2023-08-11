package kz.project.data.remote.dto.error.error_model


import com.google.gson.annotations.SerializedName

/**
 * Класс для обработки ошибки, например: неправильные данные для авторизации или время действия
 * токена истекло
 */
data class Error(
    @SerializedName("error")
    val error: String?,
    @SerializedName("error_description")
    val detail: String?
)