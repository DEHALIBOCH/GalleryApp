package kz.project.data.remote.dto.error.error_model


import com.google.gson.annotations.SerializedName

data class Violation(
    @SerializedName("message")
    val message: String?,
    @SerializedName("propertyPath")
    val propertyPath: String?
)