package kz.project.domain.use_case.user

import kz.project.domain.model.user.UpdatePasswordForm
import kz.project.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(currentUserId: Int, updatePasswordForm: UpdatePasswordForm) =
        userRepository.updatePassword(currentUserId, updatePasswordForm)
}