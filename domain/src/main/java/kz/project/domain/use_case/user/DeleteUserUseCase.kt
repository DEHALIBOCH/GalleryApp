package kz.project.domain.use_case.user

import kz.project.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(currentUserId: Int) = userRepository.deleteUser(currentUserId)
}