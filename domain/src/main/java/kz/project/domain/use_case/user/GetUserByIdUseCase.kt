package kz.project.domain.use_case.user

import kz.project.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(id: Int) = userRepository.getUserById(id)
}