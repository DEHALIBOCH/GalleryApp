package kz.project.gallery.presentation.viewmodel.profile

data class UpdatePasswordValidationForm(
    val oldPassword: String,
    val newPassword: String,
    val confirmNewPassword: String,
    val oldPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,
)
