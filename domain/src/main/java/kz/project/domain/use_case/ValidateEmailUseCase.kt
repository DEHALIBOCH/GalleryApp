package kz.project.domain.use_case

import kz.project.domain.forms_validation.EmailValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * UseCase для валидации email
 */
class ValidateEmailUseCase @Inject constructor(
    private val emailValidator: EmailValidator
) {

    operator fun invoke(email: String): ValidationResult {
        return emailValidator.execute(email)
    }
}