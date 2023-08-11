package kz.project.domain.use_case

import kz.project.domain.forms_validation.ConfirmPasswordValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * UseCase для валидации подтверждения пароля
 */
class ValidateConfirmPasswordUseCase @Inject constructor(
    private val confirmPasswordValidator: ConfirmPasswordValidator
) {

    operator fun invoke(
        password: String,
        confirmedPassword: String
    ): ValidationResult {
        return confirmPasswordValidator.execute(password, confirmedPassword)
    }
}