package kz.project.domain.use_case

import kz.project.domain.forms_validation.BirthdayValidator
import kz.project.domain.forms_validation.ValidationResult
import javax.inject.Inject

/**
 * UseCase для валидации даты рождения
 */
class ValidateBirthdayUseCase @Inject constructor(
    private val birthdayValidator: BirthdayValidator
) {

    operator fun invoke(
        birthday: String
    ): ValidationResult {
        return birthdayValidator.execute(birthday)
    }
}