package kz.project.domain.model.user

data class UpdatePasswordForm(
    val oldPassword: String,
    val newPassword: String,
)